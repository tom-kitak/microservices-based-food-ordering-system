package nl.tudelft.sem.group06b.order.service.processor;

import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.Location;
import nl.tudelft.sem.group06b.order.domain.Order;

public interface OrderProcessor {

    Long startOrder(String memberId) throws Exception;

    void setOrderTime(Long orderId, String selectedTime) throws Exception;

    void setOrderLocation(String token, Long orderId, Location location) throws Exception;

    Order placeOrder(String token, Long orderId) throws Exception;

    void cancelOrder(String token, String memberId, String roleName, Long orderId) throws Exception;

    Order fetchOrder(Long orderId) throws Exception;

    Collection<Order> fetchAllStoreOrders();

    Collection<Order> fetchAllOrders();
}
