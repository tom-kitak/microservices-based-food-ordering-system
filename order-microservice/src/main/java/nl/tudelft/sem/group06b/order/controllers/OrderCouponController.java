package nl.tudelft.sem.group06b.order.controllers;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderCouponController {

    private final transient AuthManager authManager;

    private final transient OrderService orderService;

    /**
     * Adds a coupon to the order.
     *
     * @param orderId the order id
     * @param couponId the coupon id
     * @return the result
     */
    @PostMapping("/add_coupon/{orderId}/{couponId}")
    public ResponseEntity<?> addCoupon(@PathVariable long orderId, @PathVariable String couponId) {
        try {
            orderService.addCoupon(authManager.getToken(), orderId, couponId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Removes a coupon from the order.
     *
     * @param orderId the order id
     * @return the result
     */
    @DeleteMapping("/remove_coupon/{orderId}/{couponId}")
    public ResponseEntity<?> removeCoupon(@PathVariable long orderId, @PathVariable String couponId) {
        try {
            orderService.removeCoupon(orderId, couponId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }
}
