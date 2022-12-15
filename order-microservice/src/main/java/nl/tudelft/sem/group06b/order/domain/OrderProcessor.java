package nl.tudelft.sem.group06b.order.domain;

import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderProcessor {

    private final transient OrderRepository orderRepository;

    private List<Long> activeOrders;

    /**
     * Instantiates a new OrderProcessor.
     *
     * @param orderRepository repository of orders
     */
    public OrderProcessor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.activeOrders = new ArrayList<>();
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public String startOrder(Long storeId, String selectedTime) {
        if (selectedTime == null) {
            return "Please select time";
        } else if (storeId == null) {
            return "Please select store";
        }
        String isTimeValid = isTimeValid(selectedTime);
        if (!isTimeValid.equals("VALID")) {
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

    public String changeSelectedTime(Long orderId, String selectedTime) {
        if (orderId == null) {
            return "Invalid order ID";
        } else if (!activeOrders.contains(orderId)) {
            return "No active order with this ID";
        }
        if (selectedTime == null) {
            return "Please select time";
        }
        String isTimeValid = isTimeValid(selectedTime);
        if (!isTimeValid.equals("VALID")) {
            return isTimeValid;
        }

        Order order = orderRepository.getOne(orderId);
        order.setSelectedTime(selectedTime);
        orderRepository.save(order);
        return "Time changed to " + selectedTime;
    }

    public String addPizzas(Long orderId, List<Long> pizzasIds) {
        if (orderId == null) {
            return "Invalid order ID";
        } else if (!activeOrders.contains(orderId)) {
            return "No active order with this ID";
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

    public String addCoupons(Long orderId, List<String> couponsIds) {
        if (orderId == null) {
            return "Invalid order ID";
        } else if (!activeOrders.contains(orderId)) {
            return "No active order with this ID";
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

    public String placeOrder(Long orderId) {
        if (orderId == null || !activeOrders.contains(orderId)) {
            return "No active order with this ID";
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

    public String cancelOrder(Long orderId) {
        if (orderId == null) {
            return "Invalid order ID";
        }

        // TODO
        // If you are an admin you can cancel anytime and not just 30 min before

        Order order = orderRepository.getOne(orderId);
        String isTimeValid = isTimeValid(order.getSelectedTime());
        if (!isTimeValid.equals("VALID")) {
            return "You can no longer cancel the order";
        }
        order.setStatus(Status.ORDER_CANCELED);
        orderRepository.save(order);

        // TODO
        // Notification: Notify the store about the cancellation

        return "Order canceled successfully";
    }

    private String isTimeValid(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try {
            LocalDateTime orderDate = LocalDateTime.parse(time, formatter);
            LocalDateTime currentDate = LocalDateTime.now();

            if (orderDate.minusMinutes(30).isBefore(currentDate)) {
                return "Order time has to be at least 30 minutes in the future";
            } else return "VALID";
        } catch (Exception e) {
            return "Please provide the correct time format: dd/MM/yyyy HH:mm:ss";
        }
    }
}
