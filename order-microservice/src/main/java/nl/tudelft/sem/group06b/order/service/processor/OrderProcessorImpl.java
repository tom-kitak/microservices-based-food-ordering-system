package nl.tudelft.sem.group06b.order.service.processor;

import java.util.ArrayList;
import java.util.Collection;
import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.model.ApplyCouponsToOrderModel;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import nl.tudelft.sem.group06b.order.util.TimeValidation;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessorImpl implements OrderProcessor {

    private final transient OrderRepository orderRepository;
    private final transient MenuCommunication menuCommunication;
    private final transient StoreCommunication storeCommunication;
    private final transient CouponCommunication couponCommunication;

    private final transient int deadlineOffset = 30;
    private final transient String invalidMemberId = "Invalid member ID";
    private final transient String invalidOrderId = "Invalid order ID";
    private final transient String invalidToken = "Invalid token";
    private final transient String noActiveOrderMessage = "No active order with this ID";

    /**
     * Instantiates a new orderRepository.
     *
     * @param orderRepository repository of Orders
     */
    public OrderProcessorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.menuCommunication = new MenuCommunication();
        this.storeCommunication = new StoreCommunication();
        this.couponCommunication = new CouponCommunication();
    }

    /**
     * Starts a new order.
     *
     * @param token authentication token
     * @param memberId ID of member placing the order
     * @return ID of the order created
     * @throws Exception any of the inputs is invalid
     */
    @Override
    public Long startOrder(String token, String memberId) throws Exception {
        if (token == null) {
            throw new Exception(invalidToken);
        } else if (memberId == null) {
            throw new Exception(invalidMemberId);
        }

        Order order = new Order();
        order.setStatus(Status.ORDER_ONGOING);
        order.setMemberId(memberId);
        orderRepository.save(order);

        return order.getId();
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
            throw new Exception(invalidOrderId);
        } else if (selectedTime == null || selectedTime.equals("")) {
            throw new Exception("Invalid selected time");
        } else if (orderRepository.getOne(orderId) == null
                || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        }

        TimeValidation timeValidation = new TimeValidation();
        if (!timeValidation.isTimeValid(selectedTime, deadlineOffset)) {
            throw new Exception("Selected time has to be at least " + deadlineOffset + " minutes in the future.");
        }

        Order order = orderRepository.getOne(orderId);
        order.setSelectedTime(selectedTime);
        orderRepository.save(order);
    }

    @Override
    public void setOrderLocation(String token, String location, Long orderId) throws Exception {
        if (token == null) {
            throw new Exception(invalidToken);
        } else if (location == null) {
            throw new Exception("Please enter a valid location");
        } else if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (orderRepository.getOne(orderId) == null
                || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        }

        // validate location for the store
        storeCommunication.validateLocation(location, token);

        // get the new storedID of the store with provided location
        Long storeId = storeCommunication.getStoreIdFromLocation(location, token);

        Order order = orderRepository.getOne(orderId);
        order.setStoreId(storeId);
        order.setLocation(location);
        orderRepository.save(order);
    }

    @Override
    public Order placeOrder(String token, Long orderId) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (orderRepository.getOne(orderId) == null
                || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        } else if (token == null) {
            throw  new Exception(invalidToken);
        }

        Order order = orderRepository.getOne(orderId);

        // query Menu for the prices
        if (order.getPizzas() == null || !order.getPizzas().isEmpty()) {
            throw new Exception("There are no valid pizzas in the order");
        }

        for (Pizza pizza : order.getPizzas()) {
            pizza.setPrice(menuCommunication.getPizzaPriceFromMenu(pizza, token));
        }

        // query Coupon for the discount and coupon used
        if (order.getCouponsIds() != null && !order.getCouponsIds().isEmpty()) {
            ApplyCouponsToOrderModel applyCouponsToResponse = couponCommunication.applyCouponsToOrder(order.getPizzas(),
                    order.getCouponsIds(), token);
            if (applyCouponsToResponse.getCoupons() == null || !applyCouponsToResponse.getCoupons().isEmpty()) {
                order.setCouponApplied(null);
            } else {
                order.setCouponApplied(applyCouponsToResponse.getCoupons().get(0));
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
    public void cancelOrder(String token, Long orderId) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (orderRepository.getOne(orderId) == null
                || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        }

        // TODO
        // If you are an admin you can cancel anytime and not just 30 min before

        Order order = orderRepository.getOne(orderId);

        // time validation for customer: cancellation can only be possible a set number of minutes before selected time
        TimeValidation timeValidation = new TimeValidation();
        if (!timeValidation.isTimeValid(order.getSelectedTime(), deadlineOffset)) {
            throw new Exception("You can no longer cancel the order");
        }

        order.setStatus(Status.ORDER_CANCELED);
        orderRepository.save(order);

        // notify the store about the cancellation
        String email = "Order with ID " + order.getId() + " canceled";
        storeCommunication.sendEmailToStore(order.getStoreId(), email, token);
    }

    @Override
    public Order fetchOrder() {
        return null;
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
