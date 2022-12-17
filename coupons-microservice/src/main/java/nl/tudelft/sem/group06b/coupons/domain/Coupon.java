package nl.tudelft.sem.group06b.coupons.domain;


import java.util.Date;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing a coupon and its effects.
 */
@Entity
@AllArgsConstructor
public class Coupon {
    @Id
    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private CouponType type;

    @Getter
    @Setter
    private double discount;

    @Getter
    @Setter
    private Date expirationDate;

    @ElementCollection
    @Getter
    @Setter
    private Set<String> usedBy;

    /**
     * No args constructor for JPA.
     */
    public Coupon() {
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
