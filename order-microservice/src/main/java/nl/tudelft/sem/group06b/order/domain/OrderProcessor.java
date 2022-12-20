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

    private final transient String validMessage = "VALID";
    private final transient String invalidOrderIdMessage = "Invalid order ID";
    private final transient String noActiveOrderMessage = "No active order with this ID";

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
    public String startOrder(Long storeId, String selectedTime) {
        if (selectedTime == null) {
            return "Please select time";
        } else if (storeId == null) {
            return "Please select store";
        }
        String isTimeValid = isTimeValid(selectedTime);
        if (!isTimeValid.equals(validMessage)) {
            return isTimeValid;
        }

        // TODO
        // Authenticate the store exists and send appropriate message if it doesn't

        Order order = new Order();
        order.setSelectedTime(selectedTime);
        order.setStoreId(storeId);
        order.setStatus(Status.ORDER_ONGOING);

        orderRepository.save(order);
        activeOrders.add(order.getId());
        System.out.println(order.getId());

        return "Order number " + order.getId() + " ongoing";
    }

    /**
     * Changes the time of an order.
     *
     * @param orderId id of the order
     * @param selectedTime selected time of the order
     * @return message of outcome
     */
    public String changeSelectedTime(Long orderId, String selectedTime) {
        if (orderId == null) {
            return invalidOrderIdMessage;
        } else if (!activeOrders.contains(orderId)) {
            return noActiveOrderMessage;
        }
        if (selectedTime == null) {
            return "Please select time";
        }
        String isTimeValid = isTimeValid(selectedTime);
        if (!isTimeValid.equals(validMessage)) {
            return isTimeValid;
        }

        Order order = orderRepository.getOne(orderId);
        order.setSelectedTime(selectedTime);
        orderRepository.save(order);
        return "Time changed to " + selectedTime;
    }

    /**
     * Changes the selected location of an order.
     *
     * @param orderId id of the order
     * @param storeId id of the store
     * @return message of outcome
     */
    public String changeSelectedLocation(Long orderId, Long storeId) {
        if (orderId == null) {
            return invalidOrderIdMessage;
        } else if (!activeOrders.contains(orderId)) {
            return noActiveOrderMessage;
        } else if (storeId == null) {
            return "Please select store";
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
    public String addPizzas(Long orderId, List<Long> pizzasIds) {
        if (orderId == null) {
            return invalidOrderIdMessage;
        } else if (!activeOrders.contains(orderId)) {
            return noActiveOrderMessage;
        } else if (pizzasIds == null) {
            return "Please enter valid pizzas";
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
    public String addCoupons(Long orderId, List<String> couponsIds) {
        if (orderId == null) {
            return invalidOrderIdMessage;
        } else if (!activeOrders.contains(orderId)) {
            return noActiveOrderMessage;
        } else if (couponsIds == null) {
            return "Please enter valid coupons";
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
    public String placeOrder(Long orderId) {
        if (orderId == null || !activeOrders.contains(orderId)) {
            return noActiveOrderMessage;
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
    public String cancelOrder(Long orderId) {
        if (orderId == null) {
            return invalidOrderIdMessage;
        }

        // TODO
        // If you are an admin you can cancel anytime and not just 30 min before

        Order order = orderRepository.getOne(orderId);
        String isTimeValid = isTimeValid(order.getSelectedTime());
        if (!isTimeValid.equals(validMessage)) {
            return "You can no longer cancel the order";
        }
        order.setStatus(Status.ORDER_CANCELED);
        orderRepository.save(order);

        // TODO
        // Notification: Notify the store about the cancellation

        return "Order canceled successfully";
    }

    /**
     * Checks format of provided time.
     *
     * @param time provided time
     * @return message of outcome
     */
    private String isTimeValid(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try {
            LocalDateTime orderDate = LocalDateTime.parse(time, formatter);
            LocalDateTime currentDate = LocalDateTime.now();

            if (orderDate.minusMinutes(30).isBefore(currentDate)) {
                return "Order time has to be at least 30 minutes in the future";
            } else {
                return validMessage;
            }
        } catch (Exception e) {
            return "Please provide the correct time format: dd/MM/yyyy HH:mm:ss";
        }
    }
}
