package nl.tudelft.sem.group06b.order.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessor {

    private final transient OrderRepository orderRepository;

    @Getter @Setter
    private List<Long> activeOrders;

    private final transient String valid = "VALID";
    private final transient String invalidOrderIdMessage = "Invalid order ID";
    private final transient String noActiveOrderMessage = "No active order with this ID";
    private final transient int deadlineOffset = 30;

    /**
     * Instantiates a new OrderProcessor.
     *
     * @param orderRepository repository of orders
     */
    public OrderProcessor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.activeOrders = new ArrayList<>();
    }

    /**
     * Gets order repository.
     *
     * @return order repository
     */
    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    /**
     * Starts an order.
     *
     * @param storeId id of the store
     * @param selectedTime selected time for the order
     * @return message of outcome
     */
    public String startOrder(Long storeId, String memberId, String selectedTime) throws Exception {
        if (selectedTime == null) {
            throw new Exception("Please select time");
        } else if (storeId == null) {
            throw new Exception("Please select store");
        } else if (memberId == null) {
            throw new Exception("Member ID invalid");
        }

        // validate if the time is appropriate
        String isTimeValid = isTimeValid(selectedTime, deadlineOffset);
        if (!isTimeValid.equals(valid)) {
            throw new Exception("Order has to be at least " + deadlineOffset + " minutes in the future.");
        }

        // TODO
        // Authenticate the store exists and send appropriate message if it doesn't

        Order order = new Order();
        order.setSelectedTime(selectedTime);
        order.setStoreId(storeId);
        order.setStatus(Status.ORDER_ONGOING);
        order.setMemberId(memberId);

        orderRepository.save(order);
        activeOrders.add(order.getId());
        System.out.println("Ongoing order: " + order.getId());

        return "Order number " + order.getId() + " ongoing";
    }

    /**
     * Changes the time of an order.
     *
     * @param orderId id of the order
     * @param selectedTime selected time of the order
     * @return message of outcome
     */
    public String changeSelectedTime(Long orderId, String selectedTime) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderIdMessage);
        } else if (!activeOrders.contains(orderId)) {
            throw new Exception(noActiveOrderMessage);
        }
        if (selectedTime == null) {
            throw new Exception("Please select time");
        }
        String isTimeValid = isTimeValid(selectedTime, 30);
        if (!isTimeValid.equals(valid)) {
            throw new Exception("New time has to be at least " + deadlineOffset + " minutes in the future.");
        }

        Order order = orderRepository.getOne(orderId);
        order.setSelectedTime(selectedTime);
        orderRepository.save(order);
        return "Time for order " + orderId + " changed to " + selectedTime + ".";
    }

    /**
     * Changes the selected location of an order.
     *
     * @param orderId id of the order
     * @param storeId id of the store
     * @return message of outcome
     */
    public String changeSelectedLocation(Long orderId, Long storeId) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderIdMessage);
        } else if (!activeOrders.contains(orderId)) {
            throw new Exception(noActiveOrderMessage);
        } else if (storeId == null) {
            throw new Exception("Please select store");
        }

        // TODO
        // Authenticate the store exists and send appropriate message if it doesn't

        Order order = orderRepository.getOne(orderId);
        order.setStoreId(storeId);
        orderRepository.save(order);

        return "Store changed successfully";
    }

    /**
     * Adds pizzas to an order.
     *
     * @param orderId id of the order
     * @param pizzasIds list of pizza id
     * @return message of outcome
     */
    public String addPizzas(Long orderId, List<Long> pizzasIds) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderIdMessage);
        } else if (!activeOrders.contains(orderId)) {
            throw new Exception(noActiveOrderMessage);
        } else if (pizzasIds == null) {
            throw new Exception("Please enter valid pizzas");
        }

        // TODO
        // Query menu to see if pizzas are valid
        // Menu will also communicate if the pizzas contain allergens
        // Send Notification if any of the pizzas contain allergen

        Order order = orderRepository.getOne(orderId);
        order.getPizzasIds().addAll(pizzasIds);
        orderRepository.save(order);
        return "Pizzas successfully added";
    }

    /**
     * Applies coupon to an order.
     *
     * @param orderId id of the order
     * @param couponsIds ids of the coupons entered
     * @return message of outcome
     */
    public String addCoupons(Long orderId, List<String> couponsIds) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderIdMessage);
        } else if (!activeOrders.contains(orderId)) {
            throw new Exception(noActiveOrderMessage);
        } else if (couponsIds == null) {
            throw new Exception("Please enter valid coupons");
        }

        Order order = orderRepository.getOne(orderId);
        order.getCouponsIds().addAll(couponsIds);
        orderRepository.save(order);

        //TODO
        // Some call to Coupon-microservice to see if coupons are valid

        return "Coupons successfully applied";
    }

    /**
     * Places an order.
     *
     * @param orderId id of the order
     * @return message of outcome
     */
    public String placeOrder(Long orderId) throws Exception {
        if (orderId == null || !activeOrders.contains(orderId)) {
            throw new Exception(noActiveOrderMessage);
        }

        // TODO
        // Query Menu for the prices
        // Query Coupon for the discount
        // Send the price

        Order order = orderRepository.getOne(orderId);
        order.setStatus(Status.ORDER_PLACED);
        orderRepository.save(order);
        activeOrders.remove(orderId);

        return "Order placed successfully, the price is: XXX";
    }

    /**
     * Cancels an order.
     *
     * @param orderId id of the order
     * @return message of outcome
     */
    public String cancelOrder(Long orderId) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderIdMessage);
        }

        // TODO
        // If you are an admin you can cancel anytime and not just 30 min before

        Order order = orderRepository.getOne(orderId);

        String isTimeValid = isTimeValid(order.getSelectedTime(), 30);
        if (!isTimeValid.equals(valid)) {
            throw new Exception("You can no longer cancel the order");
        }

        order.setStatus(Status.ORDER_CANCELED);
        orderRepository.save(order);

        // TODO
        // Notification: Notify the store about the cancellation

        return "Order canceled successfully";
    }

    /**
     *
     * @param time
     * @param deadlineOffset
     * @return
     * @throws Exception
     */
    private String isTimeValid(String time, int deadlineOffset) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try {
            LocalDateTime orderDate = LocalDateTime.parse(time, formatter);
            LocalDateTime currentDate = LocalDateTime.now();

            if (orderDate.minusMinutes(deadlineOffset).isBefore(currentDate)) {
                return "The time is invalid.";
            }
            return valid;
        } catch (Exception e) {
            throw new Exception("Please provide the correct time format: dd/MM/yyyy HH:mm:ss");
        }
    }
}
