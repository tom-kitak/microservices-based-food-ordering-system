package nl.tudelft.sem.group06b.coupons.service.operations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.model.ApplyCouponsRequestModel;
import nl.tudelft.sem.group06b.coupons.model.Pizza;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class CouponOperationsServiceImpl implements CouponOperationsService {
    private final transient CouponRepository couponRepository;

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
     */
    public void useCoupon(String couponId, String memberId) {
        if (!couponRepository.existsById(couponId)) {
            throw new IllegalArgumentException("Coupon does not exist");
        }
        Coupon coupon = couponRepository.getOne(couponId);
        if (coupon.getUsedBy().contains(memberId)) {
            throw new IllegalArgumentException("Coupon already used");
        }
        coupon.getUsedBy().add(memberId);
        couponRepository.save(coupon);
    }

    /**
     * Receives a list of applied coupons and prices and returns the best price and the coupon applied.
     *
     * @return a list of the best price and the coupon applied
     */
    public ApplyCouponsRequestModel calculatePrice(ApplyCouponsRequestModel pizzasAndCoupons) {
        List<Pizza> pizzas = pizzasAndCoupons.getPizzas();
        List<Coupon> couponsList = couponRepository.findAllById(pizzasAndCoupons.getCoupons());
        throwEmptyBasket(pizzas.isEmpty());
        throwNoCoupons(couponsList.isEmpty());

        Coupon discountCoupon = couponsList.stream().filter(coupon -> coupon.getType() == CouponType.DISCOUNT)
                .max(Comparator.comparing(Coupon::getDiscount)).orElse(new Coupon());
        Coupon oneOffCoupon = couponsList.stream().filter(coupon -> coupon.getType() == CouponType.ONE_OFF)
                .findFirst().orElse(new Coupon());

        Pizza pizza = pizzas.stream().max(Comparator.comparing(Pizza::getPrice)).get();
        int maxIndex = pizzas.indexOf(pizza);

        List<Coupon> coupons = List.of(discountCoupon, oneOffCoupon);
        List<List<Pizza>> pizzasList = List.of(calculateDiscountBasket(pizzas, discountCoupon),
                calculateOneOffBasket(pizzas, oneOffCoupon, maxIndex));
        int bestIndex = minimumBasket(pizzasList.get(0), pizzasList.get(1));

        return new ApplyCouponsRequestModel(pizzasList.get(bestIndex), List.of(coupons.get(bestIndex).getCode()));
    }

    private List<Pizza> calculateDiscountBasket(List<Pizza> pizzas, Coupon discountCoupon) {
        if (!discountCoupon.equals(new Coupon())) {
            return pizzas.stream().map(p -> new Pizza(
                            p.getPizzaId(),
                            p.getToppings(),
                            p.getPrice().multiply(BigDecimal.valueOf(1 - discountCoupon.getDiscount()))))
                    .collect(Collectors.toList());
        }
        return pizzas;
    }

    private List<Pizza> calculateOneOffBasket(List<Pizza> pizzas, Coupon oneOffCoupon, int maxIndex) {
        if (!oneOffCoupon.equals(new Coupon())) {
            List<Pizza> pizzasWithOneOff = pizzas.stream()
                    .map(p -> new Pizza(p.getPizzaId(), p.getToppings(), p.getPrice()))
                    .collect(Collectors.toList());
            pizzasWithOneOff.get(maxIndex).setPrice(BigDecimal.ZERO);
            return pizzasWithOneOff;
        }
        return pizzas;
    }

    private int minimumBasket(List<Pizza> pizzasWithDiscount, List<Pizza> pizzasWithOneOff) {
        BigDecimal discountPrice = pizzasWithDiscount.stream().map(Pizza::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal oneOffPrice = pizzasWithOneOff.stream().map(Pizza::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (discountPrice.compareTo(oneOffPrice) < 0) {
            return 0;
        }
        return 1;
    }

    private void throwEmptyBasket(boolean emptyBasket) {
        if (emptyBasket) {
            throw new IllegalArgumentException("The basket is empty");
        }
    }

    private void throwNoCoupons(boolean noCoupons) {
        if (noCoupons) {
            throw new IllegalArgumentException("No coupons found");
        }
    }
}

