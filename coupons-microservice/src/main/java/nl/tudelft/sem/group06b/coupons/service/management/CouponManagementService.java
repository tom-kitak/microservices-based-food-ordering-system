package nl.tudelft.sem.group06b.coupons.service.management;

import java.util.Date;
import java.util.List;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;

public interface CouponManagementService {

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
}
