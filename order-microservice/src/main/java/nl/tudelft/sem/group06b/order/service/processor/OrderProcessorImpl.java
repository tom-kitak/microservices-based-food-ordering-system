package nl.tudelft.sem.group06b.order.service.processor;

import java.util.ArrayList;
import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.Order;

public class OrderProcessorImpl implements OrderProcessor {

    @Override
    public void startOrder() {

    }

    @Override
    public void changeOrderTime() {

    }

    @Override
    public void changeOrderLocation() {

    }

    @Override
    public void placeOrder() {

    }

    @Override
    public void cancelOrder() {

    }

    @Override
    public Order fetchOrder() {
        return null;
    }

    @Override
    public Collection<Order> fetchAllStoreOrders() {
        return new ArrayList<>();
    }

    @Override
    public Collection<Order> fetchAllOrders() {
        return new ArrayList<>();
    }
}
