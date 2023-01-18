package nl.tudelft.sem.group06b.coupons.service.operations;

import nl.tudelft.sem.group06b.coupons.model.ApplyCouponsRequestModel;

public interface CouponOperationsService {

    /**
     * Checks if a coupon is in the repository and is available.
     *
     * @param couponId the id of the coupon
     * @return true if the coupon is in the repository
     */
    boolean isCouponAvailable(String couponId, String memberId);

    /**
     * Remembers that a coupon has been used by a customer.
     *
     * @param couponId the id of the coupon
     * @param memberId the id of the customer
     */
    void useCoupon(String couponId, String memberId);

    /**
     * Applies the coupons to the pizzas, chooses the optimal one to obtain the lowest price.
     *
     * @param pizzasAndCoupons the request containing the pizzas and the coupons
     * @return the pizzas with the applied coupons
     */
    ApplyCouponsRequestModel calculatePrice(ApplyCouponsRequestModel pizzasAndCoupons);
}
