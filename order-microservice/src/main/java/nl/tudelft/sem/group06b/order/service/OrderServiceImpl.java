package nl.tudelft.sem.group06b.order.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.domain.AllergenResponse;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.service.coupon.OrderCoupon;
import nl.tudelft.sem.group06b.order.service.editing.OrderEditor;
import nl.tudelft.sem.group06b.order.service.processor.OrderProcessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final transient OrderProcessor orderProcessor;
    private final transient OrderEditor orderEditor;
    private final transient OrderCoupon orderCoupon;


    @Override
    public Long startOrder(String memberId) throws Exception {
        return orderProcessor.startOrder(memberId);
    }

    @Override
    public void setOrderTime(Long orderId, String selectedTime) throws Exception {
        orderProcessor.setOrderTime(orderId, selectedTime);
    }

    @Override
    public void setOrderLocation(String token, Long orderId, String location) throws Exception {
        orderProcessor.setOrderLocation(token, orderId, location);
    }

    @Override
    public Order placeOrder(String token, Long orderId) throws Exception {
        return orderProcessor.placeOrder(token, orderId);
    }

    @Override
    public void cancelOrder(String token, String memberId, String roleName, Long orderId) throws Exception {
        orderProcessor.cancelOrder(token, memberId, roleName, orderId);
    }

    @Override
    public Order fetchOrder(Long orderId) throws Exception {
        return orderProcessor.fetchOrder(orderId);
    }

    @Override
    public Collection<Order> fetchAllStoreOrders(String token, String memberId,
                                                 String roleName, Long storeId) throws Exception {
        return orderProcessor.fetchAllStoreOrders(token, memberId, roleName, storeId);
    }

    @Override
    public Collection<Order> fetchAllOrders(String token, String memberId, String roleName) throws Exception {
        return orderProcessor.fetchAllOrders(token, memberId, roleName);
    }

    @Override
    public AllergenResponse addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception {
        return orderEditor.addPizza(token, memberId, orderId, pizza);
    }

    @Override
    public void removePizza(Long orderId, Pizza pizza) throws Exception {
        orderEditor.removePizza(orderId, pizza);
    }

    @Override
    public AllergenResponse addTopping(String token, String memberId, Long orderId,
                                           Pizza pizza, Long toppingId) throws Exception {
        return orderEditor.addTopping(token, memberId, orderId, pizza, toppingId);
    }

    @Override
    public void removeTopping(Long orderId, Pizza pizza, Long toppingId) throws Exception {
        orderEditor.removeTopping(orderId, pizza, toppingId);
    }

    @Override
    public void addCoupon(String token, Long orderId, String coupon) throws Exception {
        orderCoupon.addCoupon(token, orderId, coupon);
    }

    @Override
    public void removeCoupon(Long orderId, String coupon) throws Exception {
        orderCoupon.removeCoupon(orderId, coupon);
    }
}
