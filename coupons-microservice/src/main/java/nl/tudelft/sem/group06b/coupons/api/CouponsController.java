package nl.tudelft.sem.group06b.coupons.api;

import java.time.Instant;
import java.util.Date;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.coupons.authentication.AuthManager;
import nl.tudelft.sem.group06b.coupons.model.ApplyCouponsRequestModel;
import nl.tudelft.sem.group06b.coupons.model.NewCouponRequestModel;
import nl.tudelft.sem.group06b.coupons.service.CouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


/**
 * Controller for incoming requests of the coupons' microservice.
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
     * @param coupon the coupon to be added as a request body
     * @return ok if the coupon has been added
     */
    @PostMapping("/addCoupon")
    public ResponseEntity<?> addCoupon(@RequestBody NewCouponRequestModel coupon) {
        if (authManager.getRoles().contains(new SimpleGrantedAuthority("customer"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean added = couponsService.addCoupon(
                coupon.getCouponId(),
                coupon.getCouponType(),
                coupon.getDiscount(),
                Date.from(Instant.ofEpochMilli(coupon.getExpirationDate()))
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
        boolean available = couponsService.isCouponAvailable(code, authManager.getMemberId());
        return ResponseEntity.ok(available);
    }

    /**
     * Receives a list of applied coupons and prices and returns the best price and the coupon applied.
     *
     * @return a list of the best price and the coupon applied
     */
    @PostMapping("/calculatePrice")
    public ResponseEntity<ApplyCouponsRequestModel> calculatePrice(@RequestBody ApplyCouponsRequestModel couponsAndPizzas) {
        try {
            ApplyCouponsRequestModel returnModel = couponsService.calculatePrice(couponsAndPizzas);
            boolean res = couponsService.useCoupon(returnModel.getCoupons().get(0), authManager.getMemberId());
            if (res) {
                return ResponseEntity.ok(returnModel);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon used already");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
