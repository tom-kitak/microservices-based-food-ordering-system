package nl.tudelft.sem.group06b.coupons.domain;


import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Class representing a coupon and its effects.
 */
@Entity
public class Coupon {
    @Id
    private String code;
    private CouponType type;
    private double discount;

    /**
     * Instantiates a new Coupon.
     *
     * @param code     the coupon code
     * @param type     the type of the coupon
     * @param discount the discount if the coupon is a discount coupon
     */
    public Coupon(String code, CouponType type, double discount) {
        this.code = code;
        this.type = type;
        this.discount = discount;
    }

    /**
     * No args constructor for JPA.
     */
    public Coupon() {}

    public String getCode() {
        return code;
    }

    protected void setCode(String code) {
        this.code = code;
    }

    public CouponType getType() {
        return type;
    }

    protected void setType(CouponType type) {
        this.type = type;
    }

    public double getDiscount() {
        return discount;
    }

    protected void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * Get the effect of the coupon.
     *
     * @return The effect of the coupon. A discount percentage for a discount coupon if the coupon is a discount coupon.
     *      A -1 if the coupon is a one-off coupon.
     */
    public double getEffect() {
        if (type == CouponType.DISCOUNT) {
            return discount;
        } else {
            return -1;
        }
    }
}
