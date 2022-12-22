package nl.tudelft.sem.group06b.order.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.model.ApplyCouponsToOrderModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CouponCommunication {

    private final transient String couponUrl = "http://localhost:8083/api/coupons";

    private final transient RestTemplate restTemplate;

    public CouponCommunication() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Applies coupons to pizzas.
     *
     * @param pizzas pizzas to apply the coupons to
     * @param coupons list of pizzas to select coupon from
     * @param token authentication token
     * @return Object encapsulating the pizzas with updated prices and list with a single coupon that was applied
     */
    public ApplyCouponsToOrderModel applyCouponsToOrder(List<Pizza> pizzas, List<String> coupons, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("pizzas", pizzas);
        map.put("coupons", coupons);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<ApplyCouponsToOrderModel> response = restTemplate.postForEntity(couponUrl + "/calculatePrice",
                entity, ApplyCouponsToOrderModel.class);

        return response.getBody();
    }

    /**
     * Checks if the coupon is valid.
     *
     * @param coupon coupon to validate
     * @param token authentication token
     * @return Boolean indication validity of the coupon
     */
    public Boolean validateCoupon(String coupon, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                couponUrl + "/checkAvailability/" + coupon,
                HttpMethod.GET,
                request,
                Boolean.class
        );
        return response.getBody();
    }
}
