package nl.tudelft.sem.group06b.order.service;

import java.util.Collection;

import nl.tudelft.sem.group06b.order.domain.Allergen;
import nl.tudelft.sem.group06b.order.domain.Location;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;

public interface OrderService {

    void startOrder();

    void setOrderTime();

    void setOrderLocation();

    void placeOrder();

    void cancelOrder();

    Order fetchOrder();

    Collection<Order> fetchAllStoreOrders();

    Collection<Order> fetchAllOrders();

    Collection<Allergen> addPizza(String token, Long orderId, Pizza pizza);

    void removePizza(Long orderId, Pizza pizza);

    Collection<Allergen> addTopping(String token, Long orderId, Pizza pizza, Long toppingId);

    void removeTopping(Long orderId, Pizza pizza, Long toppingId);

    void addCoupon();

    void removeCoupon();
}
