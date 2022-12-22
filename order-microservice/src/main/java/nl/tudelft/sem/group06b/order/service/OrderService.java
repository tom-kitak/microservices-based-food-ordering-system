package nl.tudelft.sem.group06b.order.service;

import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.Allergen;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;

public interface OrderService {

    Long startOrder(String token, String memberId) throws Exception;

    void setOrderTime(Long orderId, String selectedTime) throws Exception;

    void setOrderLocation(String token, String location, Long orderId) throws Exception;

    Order placeOrder(String token, Long orderId) throws Exception;

    void cancelOrder(String token, Long orderId) throws Exception;

    Order fetchOrder();

    Collection<Order> fetchAllStoreOrders();

    Collection<Order> fetchAllOrders();

    Collection<Allergen> addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception;

    void removePizza();

    void addTopping();

    void removeTopping();

    void addCoupon(String token, Long orderId, String coupon) throws Exception;

    void removeCoupon();
}
