package nl.tudelft.sem.group06b.order.domain;

import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public String startOrder(String selectedTime, Long storeId ) {
        if (selectedTime == null) {
            return "Please select time";
        } else if (storeId == null) {
            return "Please select store";
        }

        Order order = new Order();
        order.setSelectedTime(selectedTime);
        order.setStoreId(storeId);
        order.setStatus(Status.ORDER_ONGOING);

        orderRepository.save(order);
        activeOrders.add(order.getId());
        System.out.println(order.getId());

        return "Order number " + order.getId() + " ongoing";
    }

    public String addPizzas(Long orderId, List<Long> pizzasIds) {
        if (pizzasIds == null) {
            return "Please enter valid pizzas";
        }
        if (!activeOrders.contains(orderId)) {
            return "Order has already been placed";
        }

        Order order = orderRepository.getOne(orderId);
        order.getPizzasIds().addAll(pizzasIds);
        orderRepository.save(order);
        return "Pizzas successfully added";
    }

    public String addCoupons(Long orderId, List<String> couponsIds) {
        if (couponsIds == null) {
            return "Please enter valid coupons";
        }
        if (!activeOrders.contains(orderId)) {
            return "Order has already been placed";
        }

        Order order = orderRepository.getOne(orderId);
        order.getCouponsIds().addAll(couponsIds);
        orderRepository.save(order);

        //TODO
        // Some call to Coupon-microservice to see if coupons are valid

        return "Coupons successfully applied";
    }

    public String placeOrder(Long orderId) {
        if (!activeOrders.contains(orderId)) {
            return "No active order with this ID";
        }

        Order order = orderRepository.getOne(orderId);
        order.setStatus(Status.ORDER_PLACED);
        orderRepository.save(order);
        activeOrders.remove(orderId);

        return "Order placed successfully";
    }
}
