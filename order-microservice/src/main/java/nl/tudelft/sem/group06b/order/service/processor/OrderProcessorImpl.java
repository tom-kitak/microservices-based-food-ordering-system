package nl.tudelft.sem.group06b.order.service.processor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.Builder;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.OrderBuilder;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.model.ApplyCouponsToOrderModel;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import nl.tudelft.sem.group06b.order.util.TimeValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessorImpl implements OrderProcessor {

    private static final transient String INVALID_MEMBER_ID_MESSAGE = "Invalid member ID";
    private static final transient String INVALID_ORDER_ID_MESSAGE = "Invalid order ID";
    private static final transient String INVALID_ORDER_CONTENTS_MESSAGE = "Invalid order contents";
    private static final transient String INVALID_TIME_MESSAGE =
            "Invalid time. The time should be at least 30 minutes after the current time";
    private static final transient String INVALID_LOCATION_MESSAGE = "Invalid store location";
    private static final transient String NO_ACTIVE_ORDER_MESSAGE = "No active order with this ID";
    private static final transient String INVALID_TOKEN_MESSAGE = "Invalid token";

    private static final transient int DEADLINE_OFFSET = 30;

    private final transient OrderRepository orderRepository;
    private final transient MenuCommunication menuCommunication;
    private final transient StoreCommunication storeCommunication;
    private final transient CouponCommunication couponCommunication;
    private final transient TaskScheduler taskScheduler;
    private final transient TimeValidation timeValidation;

    /**
     * Constructor for OrderProcessorImpl.
     *
     * @param orderRepository     the repository for orders
     * @param taskScheduler       the scheduler for the order deadline
     */
    @Autowired
    public OrderProcessorImpl(
            OrderRepository orderRepository,
            TaskScheduler taskScheduler,
            MenuCommunication menuCommunication,
            StoreCommunication storeCommunication,
            CouponCommunication couponCommunication,
            TimeValidation timeValidation
    ) {
        this.orderRepository = orderRepository;
        this.menuCommunication = menuCommunication;
        this.storeCommunication = storeCommunication;
        this.couponCommunication = couponCommunication;
        this.taskScheduler = taskScheduler;
        this.timeValidation = timeValidation;

        orderRepository.findAll().forEach(order -> {
            if (order.getStatus() == Status.ORDER_PLACED) {
                scheduleOrderCompletion(order.getId());
            }
        });
    }


    /**
     * Starts a new order.
     *
     * @param memberId ID of member placing the order
     * @return ID of the order created
     * @throws IllegalArgumentException if the member ID is invalid
     */
    @Override
    public Long startOrder(String memberId) throws IllegalArgumentException {
        if (memberId.isEmpty()) {
            throw new IllegalArgumentException(INVALID_MEMBER_ID_MESSAGE);
        }

        OrderBuilder orderBuilder = new OrderBuilder();
        orderBuilder.setMemberId(memberId);
        orderBuilder.setOrderStatus(Status.ORDER_ONGOING);

        Order newOrder = orderBuilder.build();
        orderRepository.save(newOrder);

        return newOrder.getId();
    }

    /**
     * Set the time of an order.
     *
     * @param orderId      ID of the order where the time will be set
     * @param selectedTime time
     * @throws Exception if any of the inputs is invalid
     */
    @Override
    public void setOrderTime(Long orderId, String selectedTime) throws Exception {
        if (orderId == null) {
            throw new IllegalArgumentException(INVALID_ORDER_ID_MESSAGE);
        }

        if (selectedTime.isEmpty() || !timeValidation.isTimeValid(selectedTime, DEADLINE_OFFSET)) {
            throw new IllegalArgumentException(INVALID_TIME_MESSAGE);
        }

        Order order = orderRepository.getOne(orderId);

        if (order.getStatus() != Status.ORDER_ONGOING) {
            throw new IllegalArgumentException(NO_ACTIVE_ORDER_MESSAGE);
        }

        OrderBuilder orderBuilder = Builder.toBuilder(order);
        orderBuilder.setOrderTime(selectedTime);

        Order newOrder = orderBuilder.build();
        orderRepository.save(newOrder);
    }

    @Override
    public void setOrderLocation(String token, Long orderId, String location) throws Exception {
        if (token.isEmpty()) {
            throw new IllegalArgumentException(INVALID_TOKEN_MESSAGE);
        }

        if (location.isEmpty() || !storeCommunication.validateLocation(location, token)) {
            throw new IllegalArgumentException(INVALID_LOCATION_MESSAGE);
        }

        Long storeId = storeCommunication.getStoreIdFromLocation(location, token);
        Order order = orderRepository.getOne(orderId);

        if (order.getStatus() != Status.ORDER_ONGOING) {
            throw new IllegalArgumentException(NO_ACTIVE_ORDER_MESSAGE);
        }

        OrderBuilder orderBuilder = Builder.toBuilder(order);
        orderBuilder.setOrderLocation(location);
        orderBuilder.setOrderStoreId(storeId);

        Order newOrder = orderBuilder.build();
        orderRepository.save(newOrder);
    }

    @Override
    public Order placeOrder(String token, Long orderId) throws IllegalArgumentException {
        if (token.isEmpty()) {
            throw new IllegalArgumentException(INVALID_TOKEN_MESSAGE);
        }

        if (orderId == null) {
            throw new IllegalArgumentException(INVALID_ORDER_ID_MESSAGE);
        }

        Order order = orderRepository.getOne(orderId);

        if (order.getPizzas() == null || order.getPizzas().isEmpty()) {
            throw new IllegalArgumentException(INVALID_ORDER_CONTENTS_MESSAGE);
        } else if (order.getSelectedTime() == null || order.getSelectedTime().isEmpty()) {
            throw new IllegalArgumentException(INVALID_ORDER_CONTENTS_MESSAGE);
        } else if (order.getStatus() == null || order.getStatus() != Status.ORDER_ONGOING) {
            throw new IllegalArgumentException(NO_ACTIVE_ORDER_MESSAGE);
        } else if (order.getLocation() == null || order.getStoreId() == null || order.getLocation().isEmpty()) {
            throw new IllegalArgumentException(INVALID_ORDER_CONTENTS_MESSAGE);
        }

        for (Pizza pizza : order.getPizzas()) {
            pizza.setPrice(menuCommunication.getPizzaPriceFromMenu(pizza, token));
        }

        OrderBuilder orderBuilder = Builder.toBuilder(order);

        if (order.getCoupons() != null && !order.getCoupons().isEmpty()) {
            ApplyCouponsToOrderModel applyCouponsToResponse = couponCommunication.applyCouponsToOrder(order.getPizzas(),
                    new ArrayList<>(order.getCoupons()), token);
            if (applyCouponsToResponse.getCoupons() == null || applyCouponsToResponse.getCoupons().isEmpty()) {
                orderBuilder.setAppliedCoupon(null);
            } else {
                orderBuilder.setAppliedCoupon(applyCouponsToResponse.getCoupons().get(0));
            }
            orderBuilder.setPizzas(applyCouponsToResponse.getPizzas());
        }

        orderBuilder.setPrice(order.calculateTotalPrice());
        orderBuilder.setOrderStatus(Status.ORDER_PLACED);
        scheduleOrderCompletion(orderId);

        Order newOrder = orderBuilder.build();
        orderRepository.save(newOrder);

        // notify the store
        storeCommunication.sendEmailToStore(order.getStoreId(), order.formatEmail(), token);

        return order;
    }

    /**
     * Schedules the order to be set to completed at the due date, if the date was changed reschedules the order.
     *
     * @param orderId ID of the order
     */
    @Override
    public void scheduleOrderCompletion(long orderId) {
        Order order = orderRepository.getOne(orderId);
        OrderBuilder orderBuilder = Builder.toBuilder(order);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime due = LocalDateTime.parse(order.getSelectedTime(), formatter);
        if (order.getStatus() == Status.ORDER_PLACED && due.isBefore(LocalDateTime.now())) {
            orderBuilder.setOrderStatus(Status.ORDER_FINISHED);
            Order newOrder = orderBuilder.build();
            orderRepository.save(newOrder);
        } else {
            taskScheduler.schedule(() ->
                    scheduleOrderCompletion(orderId), Date.from(due.atZone(ZoneId.systemDefault()).toInstant()));
        }
    }

    @Override
    public void cancelOrder(String token, String memberId, String roleName, Long orderId) throws Exception {
        if (orderId == null) {
            throw new IllegalArgumentException(INVALID_ORDER_ID_MESSAGE);
        }

        Order order = orderRepository.getOne(orderId);

        if (storeCommunication.validateManager(memberId, token)) {
            roleName = "store_admin";
        }

        switch (roleName) {
            case "regional_manager":
                if (order.getStatus() != Status.ORDER_ONGOING && order.getStatus() != Status.ORDER_PLACED) {
                    throw new IllegalArgumentException(NO_ACTIVE_ORDER_MESSAGE);
                }
                break;
            case "store_admin":
                if (order.getStatus() != Status.ORDER_ONGOING && order.getStatus() != Status.ORDER_PLACED) {
                    throw new IllegalArgumentException(NO_ACTIVE_ORDER_MESSAGE);
                }
                if (!orderRepository.getOne(orderId).getStoreId()
                        .equals(storeCommunication.getStoreIdFromManager(memberId, token))) {
                    throw new UnsupportedOperationException("Not the store manager of this store");
                }
                break;
            case "customer":
                if (!order.getMemberId().equals(memberId)) {
                    throw new UnsupportedOperationException("Access denied.");
                }
                if (order.getStatus() != Status.ORDER_ONGOING) {
                    throw new IllegalArgumentException(NO_ACTIVE_ORDER_MESSAGE);
                }
                if (!timeValidation.isTimeValid(order.getSelectedTime(), DEADLINE_OFFSET)) {
                    throw new Exception("You can no longer cancel the order");
                }
                break;
            default:
                throw new IllegalArgumentException("Role is not handled");
        }

        OrderBuilder orderBuilder = Builder.toBuilder(order);
        orderBuilder.setOrderStatus(Status.ORDER_CANCELED);

        Order newOrder = orderBuilder.build();
        orderRepository.save(newOrder);

        // notify the store about the cancellation
        String email = "Order with ID " + order.getId() + " canceled";
        storeCommunication.sendEmailToStore(order.getStoreId(), email, token);
    }

    @Override
    public Order fetchOrder(Long orderId) throws Exception {
        if (orderId == null) {
            throw new Exception(INVALID_ORDER_ID_MESSAGE);
        }
        if (orderRepository.existsById(orderId)) {
            return orderRepository.getOne(orderId);
        }
        throw new Exception(INVALID_ORDER_ID_MESSAGE);
    }

    @Override
    public Collection<Order> fetchAllStoreOrders(String token, String memberId,
                                                 String roleName, Long storeId) throws Exception {

        List<Order> orders = orderRepository.findAll().stream()
                .filter(x -> Objects.equals(x.getStoreId(), storeId)).collect(Collectors.toList());

        if (storeCommunication.validateManager(memberId, token)) {
            if (!storeId.equals(storeCommunication.getStoreIdFromManager(memberId, token))) {
                throw new UnsupportedOperationException("Not the store manager of this store");
            }
            return orders;
        } else if (roleName.equals("regional_manager")) {
            return orders;
        }

        throw new Exception("Customers can not view store orders");
    }

    @Override
    public Collection<Order> fetchAllOrders(String token, String memberId,
                                            String roleName) throws Exception {
        if (roleName.equals("regional_manager")) {
            return orderRepository.findAll();
        }
        throw new Exception("Only regional managers can view all orders");
    }
}
