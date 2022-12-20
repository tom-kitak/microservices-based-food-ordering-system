package nl.tudelft.sem.group06b.coupons.domain;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
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
    public boolean isCouponAvailable(String couponId) {
        if (!couponRepository.existsById(couponId)) {
            return false;
        }
        Coupon coupon = couponRepository.getOne(couponId);
        return coupon.getExpirationDate().after(Date.from(Instant.now()));
    }

    /**
     * Calculates the minimum price of the basket by picking the optimal coupon.
     *
     * @param prices  the prices of the items in the basket
     * @param coupons the coupons added to the order
     * @return the minimum price of the basket and the coupon used as a list of string
     */
    public List<String> calculatePrice(List<Double> prices, List<String> coupons) {
        if (!prices.isEmpty()) {
            List<Coupon> couponsList = couponRepository.findAllById(coupons);
            double price = prices.stream().mapToDouble(Double::doubleValue).sum();

            double discountPrice = price - price * couponsList.stream()
                    .filter(coupon -> coupon.getType() == CouponType.DISCOUNT)
                    .mapToDouble(Coupon::getDiscount)
                    .max().orElse(0.0);
            String discountCode = couponsList.stream()
                    .filter(coupon -> coupon.getType() == CouponType.DISCOUNT)
                    .max(Comparator.comparingDouble(Coupon::getDiscount)).map(Coupon::getCode).orElse("");

            double oneOffPrice = price - prices.stream().max(Double::compareTo).orElse(0.0);
            String oneOffCode = couponsList.stream()
                    .filter(coupon -> coupon.getType() == CouponType.ONE_OFF)
                    .map(Coupon::getCode).findFirst().orElse("");

            if (discountCode.isBlank() && oneOffCode.isBlank()) {
                return List.of(String.valueOf(price), "");
            } else if (discountCode.isBlank()) {
                return List.of(String.valueOf(oneOffPrice), oneOffCode);
            } else if (oneOffCode.isBlank()) {
                return List.of(String.valueOf(oneOffPrice), oneOffCode);
            } else {
                return List.of(
                        String.valueOf(Math.min(discountPrice, oneOffPrice)),
                        discountPrice < oneOffPrice ? discountCode : oneOffCode
                );
            }
        }

        return List.of();
    }
}
