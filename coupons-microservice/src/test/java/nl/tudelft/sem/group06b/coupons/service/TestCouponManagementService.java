package nl.tudelft.sem.group06b.coupons.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import nl.tudelft.sem.group06b.coupons.service.management.CouponManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestCouponManagementService {
    @MockBean
    private transient CouponRepository mockCouponRepository;

    @Autowired
    private transient CouponManagementService couponManagementService;

    @Test
    public void testAddCouponCorrectDiscount() {
        Date date = Date.from(Instant.now().plusSeconds(30));
        couponManagementService
                .addCoupon(
                        "test99",
                        "DISCOUNT",
                        0.5,
                        date);
        // Verify mock calls
        verify(mockCouponRepository, times(1))
                .save(new Coupon("test99", CouponType.DISCOUNT, 0.5,
                        date, new HashSet<>()
                ));
    }

    @Test
    public void testAddCouponCorrectOneOff() {
        Date date = Date.from(Instant.now().plusSeconds(30));
        couponManagementService
                .addCoupon(
                        "test99",
                        "ONEOFF",
                        0.5,
                        date);
        // Verify mock calls
        verify(mockCouponRepository, times(1))
                .save(new Coupon("test99", CouponType.ONE_OFF, 0,
                        date, new HashSet<>()
                ));
    }

    @Test
    public void testAddCouponIncorrectType() {
        Date date = Date.from(Instant.now().plusSeconds(30));
        assertThrows(IllegalArgumentException.class, () -> couponManagementService
                .addCoupon(
                        "test99",
                        "INCORRECT",
                        0.5,
                        date), "Invalid coupon type");
        // Verify mock calls
        verify(mockCouponRepository, never())
                .save(new Coupon("test99", CouponType.DISCOUNT, 0.5,
                        date, new HashSet<>()
                ));
    }

    @Test
    public void testAddCouponIncorrectCode() {
        Date date = Date.from(Instant.now().plusSeconds(30));
        assertThrows(IllegalArgumentException.class, () -> couponManagementService
                .addCoupon(
                        "test9900",
                        "DISCOUNT",
                        0.5,
                        date), "Invalid coupon id");
        // Verify mock calls
        verify(mockCouponRepository, never())
                .save(new Coupon("test9900", CouponType.DISCOUNT, 0.5,
                        date, new HashSet<>()
                ));
    }

    @Test
    public void testRemoveExistingCoupon() {
        when(mockCouponRepository.existsById("test99")).thenReturn(true);
        couponManagementService.removeCoupon("test99");
        verify(mockCouponRepository, times(1))
                .deleteById("test99");
    }

    @Test
    public void testRemoveNonExistentCoupon() {
        when(mockCouponRepository.existsById("test99")).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> couponManagementService
                .removeCoupon("test99"), "Coupon does not exist");
        verify(mockCouponRepository, never()).deleteById("test99");
    }

    @Test
    public void testQueryAllCoupons() {
        couponManagementService.queryAllCoupons();
        verify(mockCouponRepository, times(1)).findAll();
    }
}
