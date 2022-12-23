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
}
