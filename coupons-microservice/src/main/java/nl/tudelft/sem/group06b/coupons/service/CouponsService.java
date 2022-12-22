package nl.tudelft.sem.group06b.coupons.service;

import java.util.Date;
import java.util.List;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.model.ApplyCouponsRequestModel;


public interface CouponsService {


    /**
     * Adds a new coupon to the database.
     *
     * @param couponId       the id of the coupon
     * @param couponType     the type of the coupon
     * @param discount       the discount of the coupon
     * @param expirationDate the expiration date of the coupon
     */
    void addCoupon(String couponId, String couponType, double discount, Date expirationDate);

    /**
     * Removes the coupon from the database if it exists.
     *
     * @param couponId the id of the coupon
     */
    void removeCoupon(String couponId);

    /**
     * Queries the database for all coupons.
     *
     * @return all the coupons
     */
    List<Coupon> queryAllCoupons();

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
