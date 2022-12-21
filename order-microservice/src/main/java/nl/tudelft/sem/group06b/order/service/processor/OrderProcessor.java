package nl.tudelft.sem.group06b.order.service.processor;

public interface OrderProcessor {

    void startOrder();

    void changeOrderTime();

    void changeOrderLocation();

    void placeOrder();

    void cancelOrder();

    void fetchOrder();

    void fetchAllOrders();
}
