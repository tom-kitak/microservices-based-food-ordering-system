package nl.tudelft.sem.group06b.coupons.api;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.coupons.authentication.AuthManager;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.model.ApplyCouponsRequestModel;
import nl.tudelft.sem.group06b.coupons.model.NewCouponRequestModel;
import nl.tudelft.sem.group06b.coupons.service.CouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        if (!authManager.getRoles().contains(new SimpleGrantedAuthority("store_manager"))
                && !authManager.getRoles().contains(new SimpleGrantedAuthority("regional_manager"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to add coupons");
        }

        try {
            couponsService.addCoupon(
                    coupon.getCouponId(),
                    coupon.getCouponType(),
                    coupon.getDiscount(),
                    Date.from(Instant.ofEpochMilli(coupon.getExpirationDate()))
            );
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a coupon from the database if the user is admin.
     *
     * @param couponId the id of the coupon to be deleted
     * @return ok if the coupon has been deleted
     */
    @DeleteMapping("/removeCoupon/{couponId}")
    public ResponseEntity<?> removeCoupon(@PathVariable String couponId) {
        if (!authManager.getRoles().contains(new SimpleGrantedAuthority("store_manager"))
                && !authManager.getRoles().contains(new SimpleGrantedAuthority("regional_manager"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to remove coupons");
        }

        try {
            couponsService.removeCoupon(couponId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Returns a list of all coupons in the database.
     *
     * @return a list of all coupons
     */
    @GetMapping("/getCoupons")
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        if (!authManager.getRoles().contains(new SimpleGrantedAuthority("store_manager"))
                && !authManager.getRoles().contains(new SimpleGrantedAuthority("regional_manager"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to remove coupons");
        }

        List<Coupon> coupons = couponsService.queryAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    /**
     * Checks if the user has used the coupon and if the coupon is available.
     *
     * @return whether the user can use the coupon
     */
    @GetMapping("/checkAvailability/{code}")
    public ResponseEntity<?> checkAvailability(@PathVariable String code) {
        boolean available = couponsService.isCouponAvailable(code, authManager.getMemberId());
        if (!available) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon is not available");
        }
        return ResponseEntity.ok().build();
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
            couponsService.useCoupon(returnModel.getCoupons().get(0), authManager.getMemberId());

            return ResponseEntity.ok(returnModel);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
