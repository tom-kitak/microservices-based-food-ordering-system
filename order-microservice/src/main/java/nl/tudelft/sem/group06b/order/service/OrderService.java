package nl.tudelft.sem.group06b.order.service;

import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.Allergens;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.model.Identification;
import nl.tudelft.sem.group06b.order.model.editing.OrderPizza;

public interface OrderService {

    Long startOrder(String memberId) throws Exception;

    void setOrderTime(Long orderId, String selectedTime) throws Exception;

    void setOrderLocation(String token, Long orderId, String location) throws Exception;

    Order placeOrder(String token, Long orderId) throws Exception;

    void cancelOrder(String token, String memberId, String roleName, Long orderId) throws Exception;

    Order fetchOrder(Long orderId) throws Exception;

    Collection<Order> fetchAllStoreOrders(String token, String memberId, String roleName, Long storeId) throws Exception;

    Collection<Order> fetchAllOrders(String token, String memberId, String roleName) throws Exception;

    Allergens addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception;

    void removePizza(Long orderId, Pizza pizza) throws Exception;

    Allergens addTopping(Identification identification, OrderPizza orderPizza, Long toppingId) throws Exception;

    void removeTopping(Long orderId, Pizza pizza, Long toppingId) throws Exception;

    void addCoupon(String token, Long orderId, String coupon) throws Exception;

    void removeCoupon(Long orderId, String coupon) throws Exception;
}
