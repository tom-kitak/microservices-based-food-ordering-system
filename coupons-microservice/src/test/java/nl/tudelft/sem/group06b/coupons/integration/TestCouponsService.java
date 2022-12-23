package nl.tudelft.sem.group06b.coupons.integration;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import nl.tudelft.sem.group06b.coupons.service.CouponsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockCouponRepository"})
@AutoConfigureMockMvc
public class TestCouponsService {
    @Autowired
    private transient CouponsService couponsService;

    @Autowired
    private transient CouponRepository mockCouponRepository;

    @Test
    public void testAddCoupon() {
        couponsService
                .addCoupon(
                        "test99",
                        "DISCOUNT",
                        0.5,
                        Date.from(Instant.now().plusSeconds(30)));
        // Verify mock calls
        verify(mockCouponRepository, times(1))
                .save(new Coupon("test99", CouponType.DISCOUNT, 0.5,
                        Date.from(Instant.now().plusSeconds(30)), new HashSet<>()
                ));
    }
}
