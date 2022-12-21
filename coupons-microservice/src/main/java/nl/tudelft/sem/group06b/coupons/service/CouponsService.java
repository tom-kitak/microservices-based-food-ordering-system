package nl.tudelft.sem.group06b.coupons.service;

import java.util.Date;
import nl.tudelft.sem.group06b.coupons.model.ApplyCouponsRequestModel;


public interface CouponsService {


    /**
     * Adds a new coupon to the database.
     *
     * @param couponId       the id of the coupon
     * @param couponType     the type of the coupon
     * @param discount       the discount of the coupon
     * @param expirationDate the expiration date of the coupon
     * @return true if the coupon has been added
     */
    boolean addCoupon(String couponId, String couponType, double discount, Date expirationDate);

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
     * @return if the coupon has been successfully used
     */
    boolean useCoupon(String couponId, String memberId);

    /**
     * Applies the coupons to the pizzas, chooses the optimal one to obtain the lowest price.
     *
     * @param pizzasAndCoupons the request containing the pizzas and the coupons
     * @return the pizzas with the applied coupons
     */
    ApplyCouponsRequestModel calculatePrice(ApplyCouponsRequestModel pizzasAndCoupons);
}
