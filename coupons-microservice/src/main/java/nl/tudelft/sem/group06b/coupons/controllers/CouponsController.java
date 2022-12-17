package nl.tudelft.sem.group06b.coupons.controllers;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.coupons.authentication.AuthManager;
import nl.tudelft.sem.group06b.coupons.domain.CouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
@RequestMapping("/api/coupons")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CouponsController {

    private final transient AuthManager authManager;
    private final transient CouponsService couponsService;

    /**
     * Adds a new coupon to the database if the user is admin.
     *
     * @param couponId       the id of the coupon
     * @param couponType     the type of the coupon
     * @param discount       the discount of the coupon
     * @param expirationDate the expiration date of the coupon in milliseconds since epoch
     * @return ok if the coupon has been added
     */
    @PostMapping("/addCoupon")
    public ResponseEntity addCoupon(@RequestBody String couponId, @RequestBody String couponType,
                                    @RequestBody double discount, @RequestBody long expirationDate) {
        //TODO: check if the coupon already exists and if the user is admin

        boolean added = couponsService.addCoupon(
                couponId,
                couponType,
                discount,
                Date.from(Instant.ofEpochMilli(expirationDate))
        );

        return added ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Checks if the user has used the coupon and if the coupon is available.
     *
     * @return whether the user can use the coupon
     */
    @GetMapping("/checkAvailability/{code}")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable String code) {
        //TODO: extract user id from token and check it

        boolean available = couponsService.isCouponAvailable(code);
        return ResponseEntity.ok(available);
    }

    /**
     * Receives a list of applied coupons and prices and returns the best price and the coupon applied.
     *
     * @return a list of the best price and the coupon applied
     */
    @GetMapping("/calculatePrice")
    public ResponseEntity<List<String>> calculatePrice(
            @RequestParam List<Double> prices,
            @RequestParam List<String> coupons
    ) {
        List<String> result = couponsService.calculatePrice(prices, coupons);
        return result.isEmpty() ? ResponseEntity.status(HttpStatus.BAD_REQUEST).build() : ResponseEntity.ok(result);
    }

    @PostMapping("/useCoupon")
    public ResponseEntity<Boolean> useCoupon(@RequestParam String code) {
        //TODO: extract user id from token and add it to the relevant coupon as used
        return ResponseEntity.ok(false);
    }

}
