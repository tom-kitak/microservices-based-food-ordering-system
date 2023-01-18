package nl.tudelft.sem.group06b.coupons.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.model.ApplyCouponsRequestModel;
import nl.tudelft.sem.group06b.coupons.model.Pizza;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import nl.tudelft.sem.group06b.coupons.service.operations.CouponOperationsService;
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
public class TestCouponOperationsService {
    @MockBean
    private transient CouponRepository mockCouponRepository;

    @Autowired
    private transient CouponOperationsService couponOperationsService;

    @Test
    public void testIsCouponAvailable() {
        when(mockCouponRepository.existsById("test99")).thenReturn(true);
        when(mockCouponRepository.getOne("test99")).thenReturn(new Coupon("test99",
                CouponType.DISCOUNT, 0.5, Date.from(Instant.now().plusSeconds(60)),
                new HashSet<>()
        ));
        assert couponOperationsService.isCouponAvailable("test99", "test");
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
        assert !couponOperationsService.isCouponAvailable("test99", "test");
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
        assert !couponOperationsService.isCouponAvailable("test99", "test");
        verify(mockCouponRepository, times(1)).existsById("test99");
        verify(mockCouponRepository, times(1)).getOne("test99");
    }

    @Test
    public void testIsCouponAvailableNonExistent() {
        when(mockCouponRepository.existsById("test99")).thenReturn(false);
        assert !couponOperationsService.isCouponAvailable("test99", "test");
        verify(mockCouponRepository, times(1)).existsById("test99");
        verify(mockCouponRepository, never()).getOne("test99");
    }

    @Test
    public void testUseCouponSuccess() {
        Date date = Date.from(Instant.now().plusSeconds(60));
        when(mockCouponRepository.existsById("test99")).thenReturn(true);
        when(mockCouponRepository.getOne("test99")).thenReturn(new Coupon("test99",
                CouponType.DISCOUNT, 0.5, date,
                new HashSet<>()
        ));
        couponOperationsService.useCoupon("test99", "test");
        verify(mockCouponRepository, times(1)).existsById("test99");
        verify(mockCouponRepository, times(1)).getOne("test99");
        verify(mockCouponRepository, times(1)).save(new Coupon("test99",
                CouponType.DISCOUNT, 0.5, date,
                new HashSet<>(List.of("test"))
        ));
    }

    @Test
    public void testUseCouponAlreadyUsed() {
        Date date = Date.from(Instant.now().plusSeconds(60));
        when(mockCouponRepository.existsById("test99")).thenReturn(true);
        when(mockCouponRepository.getOne("test99")).thenReturn(new Coupon("test99",
                CouponType.DISCOUNT, 0.5, date,
                new HashSet<>(List.of("test"))
        ));
        assertThrows(IllegalArgumentException.class, () -> couponOperationsService
                .useCoupon("test99", "test"), "Coupon already used");
        verify(mockCouponRepository, times(1)).existsById("test99");
        verify(mockCouponRepository, times(1)).getOne("test99");
    }

    @Test
    public void testUseCouponNonExistent() {
        when(mockCouponRepository.existsById("test99")).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> couponOperationsService
                .useCoupon("test99", "test"), "Coupon does not exist");
        verify(mockCouponRepository, times(1)).existsById("test99");
        verify(mockCouponRepository, never()).getOne("test99");
    }

    @Test
    public void testCalculatePriceEmpty() {
        ApplyCouponsRequestModel requestModel = new ApplyCouponsRequestModel(new ArrayList<>(), List.of("test"));
        assertThrows(IllegalArgumentException.class, () -> couponOperationsService
                .calculatePrice(requestModel), "The basket is empty");
    }

    @Test
    public void testCalculatePriceEmptyCoupons() {
        List<Pizza> pizzas = List.of(
                new Pizza(32L, List.of(), new BigDecimal("10.00")),
                new Pizza(31L, List.of(), new BigDecimal("30.00")),
                new Pizza(11L, List.of(), new BigDecimal("40.00")));
        ApplyCouponsRequestModel requestModel = new ApplyCouponsRequestModel(pizzas, new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> couponOperationsService
                .calculatePrice(requestModel), "No coupons found");
    }

    @Test
    public void testCalculatePriceOnlyDiscount() {
        List<Pizza> pizzas = List.of(
                new Pizza(32L, List.of(), new BigDecimal("10.00")),
                new Pizza(31L, List.of(), new BigDecimal("30.00")),
                new Pizza(11L, List.of(), new BigDecimal("40.00")));
        ApplyCouponsRequestModel requestModel = new ApplyCouponsRequestModel(pizzas, List.of(
                "test",
                "test2",
                "test3"));
        when(mockCouponRepository.findAllById(List.of("test", "test2", "test3")))
                .thenReturn(List.of(
                        new Coupon("test", CouponType.DISCOUNT, 0.2, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>()),
                        new Coupon("test2", CouponType.DISCOUNT, 0.5, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>()),
                        new Coupon("test3", CouponType.DISCOUNT, 0.3, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>())
                ));
        ApplyCouponsRequestModel responseModel = couponOperationsService.calculatePrice(requestModel);
        assert responseModel.getCoupons().get(0).equals("test2");
        assert responseModel.getPizzas().get(0).getPrice().toString().equals("5.000");
        assert responseModel.getPizzas().get(1).getPrice().toString().equals("15.000");
        assert responseModel.getPizzas().get(2).getPrice().toString().equals("20.000");
    }

    @Test
    public void testCalculatePriceOnlyFree() {
        List<Pizza> pizzas = List.of(
                new Pizza(32L, List.of(), new BigDecimal("10.00")),
                new Pizza(31L, List.of(), new BigDecimal("30.00")),
                new Pizza(11L, List.of(), new BigDecimal("40.00")));
        ApplyCouponsRequestModel requestModel = new ApplyCouponsRequestModel(pizzas, List.of(
                "test",
                "test2",
                "test3"));
        when(mockCouponRepository.findAllById(List.of("test", "test2", "test3")))
                .thenReturn(List.of(
                        new Coupon("test", CouponType.ONE_OFF, 0.2, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>()),
                        new Coupon("test2", CouponType.ONE_OFF, 0.5, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>()),
                        new Coupon("test3", CouponType.ONE_OFF, 0.3, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>())
                ));
        ApplyCouponsRequestModel responseModel = couponOperationsService.calculatePrice(requestModel);
        assert responseModel.getCoupons().get(0).equals("test");
        assert responseModel.getPizzas().get(0).getPrice().toString().equals("10.00");
        assert responseModel.getPizzas().get(1).getPrice().toString().equals("30.00");
        assert responseModel.getPizzas().get(2).getPrice().equals(BigDecimal.ZERO);
    }

    @Test
    public void testCalculatePriceDiscountEfficient() {
        List<Pizza> pizzas = List.of(
                new Pizza(32L, List.of(), new BigDecimal("40.00")),
                new Pizza(31L, List.of(), new BigDecimal("40.00")),
                new Pizza(11L, List.of(), new BigDecimal("40.00")));
        ApplyCouponsRequestModel requestModel = new ApplyCouponsRequestModel(pizzas, List.of(
                "test",
                "test2",
                "test3"));
        when(mockCouponRepository.findAllById(List.of("test", "test2", "test3")))
                .thenReturn(List.of(
                        new Coupon("test", CouponType.ONE_OFF, 0.2, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>()),
                        new Coupon("test2", CouponType.DISCOUNT, 0.8, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>()),
                        new Coupon("test3", CouponType.ONE_OFF, 0.3, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>())
                ));
        ApplyCouponsRequestModel responseModel = couponOperationsService.calculatePrice(requestModel);
        assertEquals(responseModel.getCoupons().get(0), "test2");
        assertEquals(responseModel.getPizzas().get(0).getPrice().setScale(3, RoundingMode.HALF_UP).toString(), "8.000");
        assertEquals(responseModel.getPizzas().get(1).getPrice().setScale(3, RoundingMode.HALF_UP).toString(), "8.000");
        assertEquals(responseModel.getPizzas().get(2).getPrice().setScale(3, RoundingMode.HALF_UP).toString(), "8.000");
    }

    @Test
    public void testCalculatePriceOneOffEfficient() {
        List<Pizza> pizzas = List.of(
                new Pizza(32L, List.of(), new BigDecimal("40.00")),
                new Pizza(31L, List.of(), new BigDecimal("40.00")),
                new Pizza(11L, List.of(), new BigDecimal("400.00")));
        ApplyCouponsRequestModel requestModel = new ApplyCouponsRequestModel(pizzas, List.of(
                "test",
                "test2",
                "test3"));
        when(mockCouponRepository.findAllById(List.of("test", "test2", "test3")))
                .thenReturn(List.of(
                        new Coupon("test", CouponType.ONE_OFF, 0.2, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>()),
                        new Coupon("test2", CouponType.DISCOUNT, 0.8, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>()),
                        new Coupon("test3", CouponType.ONE_OFF, 0.3, Date.from(Instant.now().plusSeconds(60)),
                                new HashSet<>())
                ));
        ApplyCouponsRequestModel responseModel = couponOperationsService.calculatePrice(requestModel);
        assert responseModel.getCoupons().get(0).equals("test");
        assert responseModel.getPizzas().get(0).getPrice().setScale(3, RoundingMode.HALF_UP).toString().equals("40.000");
        assert responseModel.getPizzas().get(1).getPrice().setScale(3, RoundingMode.HALF_UP).toString().equals("40.000");
        assert responseModel.getPizzas().get(2).getPrice().equals(BigDecimal.ZERO);
    }
}
