package nl.tudelft.sem.group06b.order.service;

import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.Order;

public interface OrderService {

    void startOrder();

    void changeOrderTime();

    void changeOrderLocation();

    void placeOrder();

    void cancelOrder();

    Order fetchOrder();

    Collection<Order> fetchAllStoreOrders();

    Collection<Order> fetchAllOrders();

    void addPizza();

    void removePizza();

    void addTopping();

    void removeTopping();

    void addCoupon();

    void removeCoupon();
}
