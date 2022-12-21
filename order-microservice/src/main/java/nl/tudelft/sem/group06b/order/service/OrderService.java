package nl.tudelft.sem.group06b.order.service;

public interface OrderService {

    void startOrder();

    void changeOrderTime();

    void changeOrderLocation();

    void placeOrder();

    void cancelOrder();

    void fetchOrder();

    void fetchAllOrders();

    void addPizza();

    void removePizza();

    void addTopping();

    void removeTopping();

    void addCoupon();

    void removeCoupon();
}
