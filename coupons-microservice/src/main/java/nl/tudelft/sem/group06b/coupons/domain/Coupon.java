package nl.tudelft.sem.group06b.coupons.domain;


import java.util.Date;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing a coupon and its effects.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    @Id
    private String code;

    private CouponType type;

    private double discount;

    private Date expirationDate;

    @ElementCollection
    private Set<String> usedBy;

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
