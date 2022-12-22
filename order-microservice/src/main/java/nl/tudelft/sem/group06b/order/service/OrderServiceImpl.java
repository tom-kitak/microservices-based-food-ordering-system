package nl.tudelft.sem.group06b.order.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.domain.Allergen;
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
    public Long startOrder(String token, String memberId) throws Exception {
        return orderProcessor.startOrder(token, memberId);
    }

    @Override
    public void setOrderTime(Long orderId, String selectedTime) throws Exception {
        orderProcessor.setOrderTime(orderId, selectedTime);
    }

    @Override
    public void setOrderLocation(String token, String location, Long orderId) throws Exception {
        orderProcessor.setOrderLocation(token, location, orderId);
    }

    @Override
    public Order placeOrder(String token, Long orderId) throws Exception {
        return orderProcessor.placeOrder(token, orderId);
    }

    @Override
    public void cancelOrder(String token, Long orderId) throws Exception {
        orderProcessor.cancelOrder(token, orderId);
    }

    @Override
    public Order fetchOrder() {
        return orderProcessor.fetchOrder();
    }

    @Override
    public Collection<Order> fetchAllStoreOrders() {
        return orderProcessor.fetchAllStoreOrders();
    }

    @Override
    public Collection<Order> fetchAllOrders() {
        return orderProcessor.fetchAllOrders();
    }

    @Override
    public Collection<Allergen> addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception {
        return orderEditor.addPizza(token, memberId, orderId, pizza);
    }

    @Override
    public void removePizza() {
        orderEditor.removePizza();
    }

    @Override
    public void addTopping() {
        orderEditor.addTopping();
    }

    @Override
    public void removeTopping() {
        orderEditor.removeTopping();
    }

    @Override
    public void addCoupon(String token, Long orderId, String coupon) throws Exception {
        orderCoupon.addCoupon(token, orderId, coupon);
    }

    @Override
    public void removeCoupon() {
        orderCoupon.removeCoupon();
    }
}
