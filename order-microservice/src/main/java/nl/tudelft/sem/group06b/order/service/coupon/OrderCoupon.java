package nl.tudelft.sem.group06b.order.service.coupon;

public interface OrderCoupon {

    void addCoupon(String token, Long orderId, String coupon) throws Exception;

    void removeCoupon(Long orderId, String coupon) throws Exception;
}
