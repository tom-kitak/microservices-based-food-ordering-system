package nl.tudelft.sem.group06b.order.service.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.Location;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.model.ApplyCouponsToOrderModel;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import nl.tudelft.sem.group06b.order.util.TimeValidation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
    private final transient TimeValidation timeValidation;




    /**
     * Starts a new order.
     *
     * @param memberId ID of member placing the order
     * @return ID of the order created
     * @throws Exception any of the inputs is invalid
     */
    @Override
    public Long startOrder(String memberId) throws IllegalArgumentException {
        if (memberId.isEmpty()) {
            throw new IllegalArgumentException(INVALID_MEMBER_ID_MESSAGE);
        }

        Order newOrder = new Order(memberId, Status.ORDER_ONGOING);
        orderRepository.save(newOrder);

        return newOrder.getId();
    }

    /**
     * Set the time of an order.
     *
     * @param orderId ID of the order where the time will be set
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

        order.setSelectedTime(selectedTime);
        orderRepository.save(order);
    }

    @Override
    public void setOrderLocation(String token, Long orderId, Location location) throws Exception {
        if (token.isEmpty()) {
            throw new IllegalArgumentException(INVALID_TOKEN_MESSAGE);
        }

        if (location.getAddress().isEmpty() && storeCommunication.validateLocation(location.getAddress(), token)) {
            throw new IllegalArgumentException(INVALID_LOCATION_MESSAGE);
        }

        Long storeId = storeCommunication.getStoreIdFromLocation(location.getAddress(), token);
        Order order = orderRepository.getOne(orderId);

        if (order.getStatus() != Status.ORDER_ONGOING) {
            throw new IllegalArgumentException(NO_ACTIVE_ORDER_MESSAGE);
        }

        order.setStoreId(storeId);
        order.setLocation(location.getAddress());
        orderRepository.save(order);
    }

    @Override
    public Order placeOrder(String token, Long orderId) throws Exception {
        if (token.isEmpty()) {
            throw new IllegalArgumentException(INVALID_TOKEN_MESSAGE);
        }

        if (orderId == null) {
            throw new IllegalArgumentException(INVALID_ORDER_ID_MESSAGE);
        }

        Order order = orderRepository.getOne(orderId);

        if (order.getPizzas().isEmpty()) {
            throw new IllegalArgumentException(INVALID_ORDER_CONTENTS_MESSAGE);
        }

        for (Pizza pizza : order.getPizzas()) {
            pizza.setPrice(menuCommunication.getPizzaPriceFromMenu(pizza, token));
        }

        if (order.getAppliedCoupon().isEmpty()) {
            // TODO: extract logic to an external class to generate request
            ApplyCouponsToOrderModel applyCouponsToResponse = couponCommunication.applyCouponsToOrder(order.getPizzas(),
                    List.of(order.getAppliedCoupon()), token);
            if (applyCouponsToResponse.getCoupons() == null || !applyCouponsToResponse.getCoupons().isEmpty()) {
                order.setAppliedCoupon(null);
            } else {
                order.setAppliedCoupon(applyCouponsToResponse.getCoupons().get(0));
            }
            order.setPizzas(applyCouponsToResponse.getPizzas());
        }

        order.calculateTotalPrice();
        order.setStatus(Status.ORDER_PLACED);
        orderRepository.save(order);

        // notify the store
        storeCommunication.sendEmailToStore(order.getStoreId(), order.formatEmail(), token);

        return order;
    }

    @Override
    public void cancelOrder(String token, String memberId, String roleName, Long orderId) throws Exception {
        if (orderId == null) {
            throw new IllegalArgumentException(INVALID_ORDER_ID_MESSAGE);
        }

        Order order = orderRepository.getOne(orderId);

        switch (roleName) {
            case "regional_manager":
                if (order.getStatus() != Status.ORDER_ONGOING || order.getStatus() != Status.ORDER_PLACED) {
                    throw new IllegalArgumentException(NO_ACTIVE_ORDER_MESSAGE);
                }
                break;
            case "store_admin":
                //TODO: set proper access methods for the store: there is an endpoint for it

                //if (order.getStoreId().equals(memberId)) {
                //
                //}
                if (order.getStatus() != Status.ORDER_ONGOING || order.getStatus() != Status.ORDER_PLACED) {
                    throw new IllegalArgumentException(NO_ACTIVE_ORDER_MESSAGE);
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

        order.setStatus(Status.ORDER_CANCELED);
        orderRepository.save(order);

        // notify the store about the cancellation
        String email = "Order with ID " + order.getId() + " canceled";
        storeCommunication.sendEmailToStore(order.getStoreId(), email, token);
    }

    @Override
    public Order fetchOrder(Long orderId) throws Exception {
        if (orderId == null) {
            throw new Exception(INVALID_ORDER_ID_MESSAGE);
        }
        return orderRepository.getOne(orderId);
    }

    @Override
    public Collection<Order> fetchAllStoreOrders() {
        return new ArrayList<>();
    }

    @Override
    public Collection<Order> fetchAllOrders() {
        return new ArrayList<>();
    }
}
