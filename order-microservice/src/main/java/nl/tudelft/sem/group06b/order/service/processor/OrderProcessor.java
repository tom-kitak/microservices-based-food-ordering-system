package nl.tudelft.sem.group06b.order.service.processor;

import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.Order;

public interface OrderProcessor {

    Long startOrder(String token, String memberId) throws Exception;

    void setOrderTime(Long orderId, String selectedTime) throws Exception;

    void setOrderLocation(String token, String location, Long orderId) throws Exception;

    Order placeOrder(String token, Long orderId) throws Exception;

    void cancelOrder(String token, Long orderId) throws Exception;

    Order fetchOrder();

    Collection<Order> fetchAllStoreOrders();

    Collection<Order> fetchAllOrders();
}
