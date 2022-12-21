package nl.tudelft.sem.group06b.order.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.domain.Order;
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
    public void addPizza() {
        orderEditor.addPizza();
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
    public void addCoupon() {
        orderCoupon.addCoupon();
    }

    @Override
    public void removeCoupon() {
        orderCoupon.removeCoupon();
    }
}
