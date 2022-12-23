package nl.tudelft.sem.group06b.coupons.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
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
public class TestCouponsService {
    @MockBean
    private transient CouponRepository mockCouponRepository;

    @Autowired
    private transient CouponsService couponsService;

    @Test
    public void testAddCouponCorrectDiscount() {
        Date date = Date.from(Instant.now().plusSeconds(30));
        couponsService
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
        couponsService
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
        assertThrows(IllegalArgumentException.class, () -> couponsService
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
        assertThrows(IllegalArgumentException.class, () -> couponsService
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
        couponsService.removeCoupon("test99");
        verify(mockCouponRepository, times(1))
                .deleteById("test99");
    }

    @Test
    public void testRemoveNonExistentCoupon() {
        when(mockCouponRepository.existsById("test99")).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> couponsService
                .removeCoupon("test99"), "Coupon does not exist");
        verify(mockCouponRepository, never()).deleteById("test99");
    }

    @Test
    public void testQueryAllCoupons() {
        couponsService.queryAllCoupons();
        verify(mockCouponRepository, times(1)).findAll();
    }

    @Test
    public void testIsCouponAvailable() {
        when(mockCouponRepository.existsById("test99")).thenReturn(true);
        when(mockCouponRepository.getOne("test99")).thenReturn(new Coupon("test99",
                CouponType.DISCOUNT, 0.5, Date.from(Instant.now().plusSeconds(60)),
                new HashSet<>()
        ));
        assert couponsService.isCouponAvailable("test99", "test");
        verify(mockCouponRepository, times(1)).existsById("test99");
        verify(mockCouponRepository, times(1)).getOne("test99");
    }

    @Test
    public void testIsCouponAvailableExpired() {
        when(mockCouponRepository.existsById("test99")).thenReturn(true);
        when(mockCouponRepository.getOne("test99")).thenReturn(new Coupon("test99",
                CouponType.DISCOUNT, 0.5, Date.from(Instant.now().minusSeconds(60)),
                new HashSet<>()
        ));
        assert !couponsService.isCouponAvailable("test99", "test");
        verify(mockCouponRepository, times(1)).existsById("test99");
        verify(mockCouponRepository, times(1)).getOne("test99");
    }

    @Test
    public void testIsCouponAvailableUsed() {
        when(mockCouponRepository.existsById("test99")).thenReturn(true);
        when(mockCouponRepository.getOne("test99")).thenReturn(new Coupon("test99",
                CouponType.DISCOUNT, 0.5, Date.from(Instant.now().plusSeconds(60)),
                new HashSet<>(List.of("test"))
        ));
        assert !couponsService.isCouponAvailable("test99", "test");
        verify(mockCouponRepository, times(1)).existsById("test99");
        verify(mockCouponRepository, times(1)).getOne("test99");
    }

    @Test
    public void testIsCouponAvailableNonExistent() {
        when(mockCouponRepository.existsById("test99")).thenReturn(false);
        assert !couponsService.isCouponAvailable("test99", "test");
        verify(mockCouponRepository, times(1)).existsById("test99");
        verify(mockCouponRepository, never()).getOne("test99");
    }

    @Test
    public void testUseCouponSuccess() {

    }
}
