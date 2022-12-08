package nl.tudelft.sem.group06b.coupons.domain;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
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

    private Date expirationDate;

    @ElementCollection
    private Set<String> usedBy;

    /**
     * Instantiates a new Coupon.
     *
     * @param code     the coupon code
     * @param type     the type of the coupon
     * @param discount the discount if the coupon is a discount coupon
     */
    public Coupon(String code, CouponType type, double discount, Date expirationDate, Set<String> usedBy) {
        this.code = code;
        this.type = type;
        this.discount = discount;
        this.expirationDate = expirationDate;
        this.usedBy = usedBy;
    }

    /**
     * No args constructor for JPA.
     */
    public Coupon() {}

    /**
     * Gets coupon code.
     *
     * @return the coupon code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets coupon type.
     */
    protected void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets coupon type.
     *
     * @return the coupon type
     */
    public CouponType getType() {
        return type;
    }

    /**
     * Sets coupon type.
     */
    protected void setType(CouponType type) {
        this.type = type;
    }

    /**
     * Gets discount.
     *
     * @return the discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Sets discount.
     */
    protected void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * Gets expiration date.
     *
     * @return the expiration date
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets expiration date.
     */
    protected void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Gets the list of ids of users who used the coupon.
     *
     * @return the used by
     */
    public Set<String> getUsedBy() {
        return usedBy;
    }

    /**
     * Sets the list of ids of users who used the coupon.
     */
    protected void setUsedBy(Set<String> usedBy) {
        this.usedBy = usedBy;
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
