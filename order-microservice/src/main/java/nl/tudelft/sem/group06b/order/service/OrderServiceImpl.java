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
    public void startOrder() {
        orderProcessor.startOrder();
    }

    @Override
    public void changeOrderTime() {
        orderProcessor.changeOrderTime();
    }

    @Override
    public void changeOrderLocation() {
        orderProcessor.changeOrderLocation();
    }

    @Override
    public void placeOrder() {
        orderProcessor.placeOrder();
    }

    @Override
    public void cancelOrder() {
        orderProcessor.cancelOrder();
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
    public Collection<Allergen> addPizza(String token, Long orderId, Pizza pizza) {
        return orderEditor.addPizza(token, orderId, pizza);
    }

    @Override
    public void removePizza(Long orderId, Pizza pizza) {
        orderEditor.removePizza(orderId, pizza);
    }

    @Override
    public Collection<Allergen> addTopping(String token, Long orderId, Pizza pizza, Long toppingId) {
        return orderEditor.addTopping(token, orderId, pizza, toppingId);
    }

    @Override
    public void removeTopping(Long orderId, Pizza pizza, Long toppingId) {
        orderEditor.removeTopping(orderId, pizza, toppingId);
    }

    @Override
    public void addCoupon() {
        orderCoupon.addCoupon();
    }

    @Override
    public void removeCoupon() {
        orderCoupon.removeCoupon();
    }
}
