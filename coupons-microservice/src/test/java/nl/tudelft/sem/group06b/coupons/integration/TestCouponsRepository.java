package nl.tudelft.sem.group06b.coupons.integration;

import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCouponsRepository {
    @Autowired
    private transient CouponRepository couponRepository;

    @Test
    public void addTest() {
        assert couponRepository.findById("1").isEmpty();
        couponRepository.save(new Coupon("1", CouponType.DISCOUNT, 0.5,
                Date.from(Instant.now().plusSeconds(30)), new HashSet<>()));
        assert couponRepository.findById("1").isPresent();
    }

    @Test
    public void deleteTest() {
        couponRepository.save(new Coupon("1", CouponType.DISCOUNT, 0.5,
                Date.from(Instant.now().plusSeconds(30)), new HashSet<>()));
        couponRepository.deleteById("1");
        assert couponRepository.findById("1").isEmpty();
    }
}
