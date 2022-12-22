package nl.tudelft.sem.group06b.order.service.processor;

import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.Order;

public interface OrderProcessor {

    Long startOrder(String token, String memberId) throws Exception;

    void setOrderTime(Long orderId, String selectedTime) throws Exception;

    void setOrderLocation(String token, String location, Long orderId) throws Exception;

    void placeOrder();

    void cancelOrder();

    Order fetchOrder();

    Collection<Order> fetchAllStoreOrders();

    Collection<Order> fetchAllOrders();
}
