package nl.tudelft.sem.group06b.order.service;

import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.AllergenResponse;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;

public interface OrderService {

    Long startOrder(String token, String memberId) throws Exception;

    void setOrderTime(Long orderId, String selectedTime) throws Exception;

    void setOrderLocation(String token, String location, Long orderId) throws Exception;

    Order placeOrder(String token, Long orderId) throws Exception;

    void cancelOrder(String token, Long orderId) throws Exception;

    Order fetchOrder(Long orderId) throws Exception;

    Collection<Order> fetchAllStoreOrders();

    Collection<Order> fetchAllOrders();

    AllergenResponse addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception;

    void removePizza(Long orderId, Pizza pizza) throws Exception;

    AllergenResponse addTopping(String token, Long orderId, Long toppingId, Pizza pizza, String memberId) throws Exception;

    void removeTopping(Long orderId, Long toppingId, Pizza pizza) throws Exception;

    void addCoupon(String token, Long orderId, String coupon) throws Exception;

    void removeCoupon(Long orderId, String coupon) throws Exception;
}
