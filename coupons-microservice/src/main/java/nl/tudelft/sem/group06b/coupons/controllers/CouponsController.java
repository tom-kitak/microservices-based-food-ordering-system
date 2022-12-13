package nl.tudelft.sem.group06b.coupons.controllers;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import nl.tudelft.sem.group06b.coupons.authentication.AuthManager;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
     * Adds a new coupon to the database if the user is admin.
     *
     * @param couponId       the id of the coupon
     * @param couponType     the type of the coupon
     * @param discount       the discount of the coupon
     * @param expirationDate the expiration date of the coupon
     * @return if the coupon has been added
     */
    @PostMapping("/addCoupon")
    public ResponseEntity<Boolean> addCoupon(@RequestParam String couponId, @RequestParam CouponType couponType,
                                             @RequestParam double discount, @RequestParam Date expirationDate) {
        //TODO: check if the coupon already exists and if the user is admin
        //Check if the coupon id is in the following format: (([a-z]|[A-Z]){4}[0-9]{2})
        if (couponId.matches("(([a-z]|[A-Z]){4}[0-9]{2})")) {
            Coupon coupon = new Coupon(couponId, couponType, discount, expirationDate, new HashSet<>());
            couponRepository.save(coupon);
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Checks if the user has used the coupon and if the coupon is available.
     *
     * @return whether the user can use the coupon
     */
    @GetMapping("/couponAvailable")
    public ResponseEntity<Boolean> isCouponAvailable(@RequestParam String code) {
        //TODO: extract user id from token and check it
        if (!couponRepository.existsById(code)) {
            return ResponseEntity.ok(false);
        }
        if (couponRepository.getOne(code).getExpirationDate().after(Date.from(Instant.now()))) {
            couponRepository.deleteById(code);
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
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

    @PostMapping("/useCoupon")
    public ResponseEntity<Boolean> useCoupon(@RequestParam String code) {
        //TODO: extract user id from token and add it to the relevant coupon as used
        return ResponseEntity.ok(false);
    }

}
