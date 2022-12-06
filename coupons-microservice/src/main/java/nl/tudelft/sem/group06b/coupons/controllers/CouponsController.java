package nl.tudelft.sem.group06b.coupons.controllers;

import java.util.Comparator;
import java.util.List;
import nl.tudelft.sem.group06b.coupons.authentication.AuthManager;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class CouponsController {

    private final transient AuthManager authManager;
    private final transient CouponRepository couponRepository;

    /**
     * Instantiates a new controller.
     *
     * @param authManager      Spring Security component used to authenticate and authorize the user
     * @param couponRepository Spring Data component used to access the coupon database
     */
    @Autowired
    public CouponsController(AuthManager authManager, CouponRepository couponRepository) {
        this.authManager = authManager;
        this.couponRepository = couponRepository;
    }

    /**
     * Checks if the user has used a coupon.
     *
     * @return whether the user can use the coupon
     */
    @GetMapping("/couponAvailable")
    public ResponseEntity<Boolean> isCouponAvailable(@RequestParam String code) {
        return ResponseEntity.ok(couponRepository.existsById(code));
    }

    /**
     * Receives a list of applied coupons and prices and returns the best price and the coupon applied.
     *
     * @return a list of the best price and the coupon applied
     */
    @GetMapping("/calculateMinPrice")
    public ResponseEntity<List<String>> calculateMinPrice(
            @RequestParam List<Double> prices,
            @RequestParam List<String> coupons
    ) {
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
                return ResponseEntity.ok(List.of(String.valueOf(price), ""));
            } else if (discountCode.isBlank()) {
                return ResponseEntity.ok(List.of(String.valueOf(oneOffPrice), oneOffCode));
            } else if (oneOffCode.isBlank()) {
                return ResponseEntity.ok(List.of(String.valueOf(discountPrice), discountCode));
            } else {
                return ResponseEntity.ok(List.of(
                        String.valueOf(Math.min(discountPrice, oneOffPrice)),
                        discountPrice < oneOffPrice ? discountCode : oneOffCode
                ));
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
