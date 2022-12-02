package nl.tudelft.sem.template.example.domain;

/**
 * Class representing a coupon and its effects.
 */
public class Coupon {
    private final String code;
    private final CouponType type;
    private final double discount;

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

    public String getCode() {
        return code;
    }

    public CouponType getType() {
        return type;
    }

    public double getDiscount() {
        return discount;
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
