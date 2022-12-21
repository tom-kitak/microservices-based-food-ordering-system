package nl.tudelft.sem.group06b.coupons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCouponRequestModel {
    String couponId;
    String couponType;
    double discount;
    long expirationDate;
}
