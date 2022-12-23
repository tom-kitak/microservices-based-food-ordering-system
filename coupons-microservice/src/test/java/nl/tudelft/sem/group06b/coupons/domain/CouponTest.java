package nl.tudelft.sem.group06b.coupons.domain;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

public class CouponTest {
    @Test
    public void testGetCode() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, null);
        assert coupon.getCode().equals("test");
    }

    @Test
    public void testSetCode() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, null);
        coupon.setCode("test2");
        assert coupon.getCode().equals("test2");
    }

    @Test
    public void testGetType() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, null);
        assert coupon.getType().equals(CouponType.DISCOUNT);
    }

    @Test
    public void testSetType() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, null);
        coupon.setType(CouponType.ONE_OFF);
        assert coupon.getType().equals(CouponType.ONE_OFF);
    }

    @Test
    public void testGetDiscount() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, null);
        assert coupon.getDiscount() == 0.5;
    }

    @Test
    public void testSetDiscount() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, null);
        coupon.setDiscount(0.3);
        assert coupon.getDiscount() == 0.3;
    }

    @Test
    public void testGetExpirationDate() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, null);
        assert coupon.getExpirationDate() == null;
    }

    @Test
    public void testSetExpirationDate() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, null);
        Date date = Date.from(Instant.now());
        coupon.setExpirationDate(date);
        assert coupon.getExpirationDate().equals(date);
    }

    @Test
    public void testGetUsedBy() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, new HashSet<>());
        assert coupon.getUsedBy().equals(new HashSet<>());
    }

    @Test
    public void testSetUsedBy() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, new HashSet<>());
        HashSet<String> usedBy = new HashSet<>();
        usedBy.add("test");
        coupon.setUsedBy(usedBy);
        assert coupon.getUsedBy().equals(usedBy);
    }

    @Test
    public void testEquals() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, new HashSet<>());
        Coupon coupon2 = new Coupon("test", CouponType.DISCOUNT, 0.5, null, new HashSet<>());
        assert coupon.equals(coupon2);
    }

    @Test
    public void testHashCode() {
        Coupon coupon = new Coupon("test", CouponType.DISCOUNT, 0.5, null, new HashSet<>());
        Coupon coupon2 = new Coupon("test", CouponType.DISCOUNT, 0.5, null, new HashSet<>());
        assert coupon.hashCode() == coupon2.hashCode();
    }
}
