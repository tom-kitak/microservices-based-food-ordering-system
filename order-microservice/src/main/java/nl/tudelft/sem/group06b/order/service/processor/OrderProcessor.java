package nl.tudelft.sem.group06b.order.service.processor;

import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.Order;

public interface OrderProcessor {

    void startOrder();

    void changeOrderTime();

    void changeOrderLocation();

    void placeOrder();

    void cancelOrder();

    Order fetchOrder();

    Collection<Order> fetchAllStoreOrders();

    Collection<Order> fetchAllOrders();
}
