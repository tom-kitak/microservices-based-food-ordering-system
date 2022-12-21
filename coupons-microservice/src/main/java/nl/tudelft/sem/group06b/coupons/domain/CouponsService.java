package nl.tudelft.sem.group06b.coupons.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.coupons.model.ApplyCouponsRequestModel;
import nl.tudelft.sem.group06b.coupons.model.Pizza;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CouponsService {
    private final transient CouponRepository couponRepository;


    /**
     * Adds a new coupon to the database.
     *
     * @param couponId       the id of the coupon
     * @param couponType     the type of the coupon
     * @param discount       the discount of the coupon
     * @param expirationDate the expiration date of the coupon
     * @return true if the coupon has been added
     */
    public boolean addCoupon(String couponId, String couponType, double discount, Date expirationDate) {
        if (couponId.matches("(([a-z]|[A-Z]){4}[0-9]{2})")) {
            if (couponType.equals("DISCOUNT")) {
                couponRepository.save(new Coupon(couponId, CouponType.DISCOUNT, discount, expirationDate, new HashSet<>()));
            } else if (couponType.equals("ONEOFF")) {
                couponRepository.save(new Coupon(couponId, CouponType.ONE_OFF, 0, expirationDate, new HashSet<>()));
            } else {
                return false;
            }
            return true;
        }

        return false;
    }

    /**
     * Checks if a coupon is in the repository and is available.
     *
     * @param couponId the id of the coupon
     * @return true if the coupon is in the repository
     */
    public boolean isCouponAvailable(String couponId, String memberId) {
        if (!couponRepository.existsById(couponId)) {
            return false;
        }
        Coupon coupon = couponRepository.getOne(couponId);
        if (coupon.getUsedBy().contains(memberId)) {
            return false;
        }
        return coupon.getExpirationDate().after(Date.from(Instant.now()));
    }

    /**
     * Remembers that a coupon has been used by a customer.
     *
     * @param couponId the id of the coupon
     * @param memberId the id of the customer
     * @return if the coupon has been successfully used
     */
    public boolean useCoupon(String couponId, String memberId) {
        if (!couponRepository.existsById(couponId)) {
            return false;
        }
        Coupon coupon = couponRepository.getOne(couponId);
        if (coupon.getUsedBy().contains(memberId)) {
            return false;
        }
        coupon.getUsedBy().add(memberId);
        couponRepository.save(coupon);
        return true;
    }

    /**
     * Applies the coupons to the pizzas, chooses the optimal one to obtain the lowest price.
     *
     * @param pizzasAndCoupons the request containing the pizzas and the coupons
     */
    public void calculatePrice(ApplyCouponsRequestModel pizzasAndCoupons) {
        List<Pizza> pizzas = pizzasAndCoupons.getPizzas();
        if (!pizzas.isEmpty()) {
            List<Coupon> couponsList = couponRepository.findAllById(pizzasAndCoupons.getCoupons());
            if (couponsList.isEmpty()) {
                return;
            }

            Coupon discountCoupon = couponsList.stream()
                    .filter(coupon -> coupon.getType() == CouponType.DISCOUNT)
                    .max(Comparator.comparing(Coupon::getDiscount))
                    .orElse(null);
            Coupon oneOffCoupon = couponsList.stream()
                    .filter(coupon -> coupon.getType() == CouponType.ONE_OFF)
                    .findFirst()
                    .orElse(null);

            BigDecimal totalPrice = pizzas.stream()
                    .map(Pizza::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal bigPrice = pizzas.stream()
                    .map(Pizza::getPrice)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            // Find the index of the pizza with the highest price
            int index = pizzas.indexOf(pizzas.stream()
                    .filter(pizza -> pizza.getPrice().equals(bigPrice))
                    .findFirst()
                    .orElse(null));

            if (discountCoupon != null && oneOffCoupon != null) {
                if (totalPrice.multiply(BigDecimal.valueOf(discountCoupon.getDiscount()))
                        .compareTo(totalPrice.subtract(bigPrice)) < 0) {
                    pizzasAndCoupons.setCoupons(List.of(discountCoupon.getCode()));
                    pizzasAndCoupons.getPizzas()
                            .forEach(
                                    pizza -> pizza.setPrice(
                                            pizza.getPrice().multiply(BigDecimal.valueOf(discountCoupon.getDiscount()))
                                    ));
                } else {
                    pizzasAndCoupons.setCoupons(List.of(oneOffCoupon.getCode()));
                    pizzas.get(index).setPrice(BigDecimal.ZERO);
                }
            } else if (discountCoupon != null) {
                pizzasAndCoupons.setCoupons(List.of(discountCoupon.getCode()));
                pizzasAndCoupons.getPizzas()
                        .forEach(
                                pizza -> pizza.setPrice(
                                        pizza.getPrice().multiply(BigDecimal.valueOf(discountCoupon.getDiscount()))
                                ));
            } else if (oneOffCoupon != null) {
                pizzasAndCoupons.setCoupons(List.of(oneOffCoupon.getCode()));
                pizzas.get(index).setPrice(BigDecimal.ZERO);
            }
            return;
        }

        throw new IllegalArgumentException("The basket is empty");
    }
}
