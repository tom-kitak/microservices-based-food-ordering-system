package nl.tudelft.sem.group06b.coupons.service.management;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class CouponManagementServiceImpl implements CouponManagementService {
    private final transient CouponRepository couponRepository;

    /**
     * Adds a new coupon to the database.
     *
     * @param couponId       the id of the coupon
     * @param couponType     the type of the coupon
     * @param discount       the discount of the coupon
     * @param expirationDate the expiration date of the coupon
     */
    public void addCoupon(String couponId, String couponType, double discount, Date expirationDate) {
        if (couponId.matches("(([a-z]|[A-Z]){4}[0-9]{2})")) {
            if (couponType.equals("DISCOUNT")) {
                couponRepository.save(new Coupon(couponId, CouponType.DISCOUNT, discount, expirationDate, new HashSet<>()));
            } else if (couponType.equals("ONEOFF")) {
                couponRepository.save(new Coupon(couponId, CouponType.ONE_OFF, 0, expirationDate, new HashSet<>()));
            } else {
                throw new IllegalArgumentException("Invalid coupon type");
            }
            return;
        }

        throw new IllegalArgumentException("Invalid coupon id");
    }

    /**
     * Removes the coupon from the database if it exists.
     *
     * @param couponId the id of the coupon
     */
    public void removeCoupon(String couponId) {
        if (couponRepository.existsById(couponId)) {
            couponRepository.deleteById(couponId);
        } else {
            throw new IllegalArgumentException("Coupon does not exist");
        }
    }

    /**
     * Queries the database for all coupons.
     *
     * @return all the coupons
     */
    @Override
    public List<Coupon> queryAllCoupons() {
        return couponRepository.findAll();
    }
}
