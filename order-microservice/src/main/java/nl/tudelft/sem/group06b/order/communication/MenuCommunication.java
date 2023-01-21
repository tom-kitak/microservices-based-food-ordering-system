package nl.tudelft.sem.group06b.order.communication;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MenuCommunication {

    private final transient String menuUrl = "http://localhost:8086/api/menu";

    private final transient RestTemplate restTemplate;

    public MenuCommunication() {
        this.restTemplate = new RestTemplate();
    }
    public MenuCommunication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Validates the pizza.
     *
     * @param pizza pizza to validate
     * @param token authentication token
     * @throws Exception if pizza or response is not valid
     */
    public void validatePizza(Pizza pizza, String token) throws Exception {
        HttpHeaders headers = makeHeader(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("id", pizza.getPizzaId());
        map.put("toppingIds", pizza.getToppings());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(menuUrl + "/isValid", entity, Boolean.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == false) {
            throw new Exception("Pizza " + pizza.getPizzaId() + " is not valid");
        }
    }

    /**
     * Validates the topping.
     *
     * @param toppingId ID of the topping to check
     * @param token authentication token
     * @throws Exception when the topping is not valid
     */
    public void validateTopping(Long toppingId, String token) throws Exception {
        HttpHeaders headers = makeHeader(token);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                menuUrl + "/isValidTopping/" + toppingId,
                HttpMethod.GET,
                request,
                Boolean.class
        );
        if (response.getStatusCode() != HttpStatus.OK || !response.getBody()) {
            throw new Exception("Topping is not valid");
        }
    }

    /**
     * Price of the pizza without any coupons.
     *
     * @param pizza pizza for which price will be returned
     * @param token authentication token
     * @return price of the pizza
     */
    public BigDecimal getPizzaPriceFromMenu(Pizza pizza, String token) {
        HttpHeaders headers = makeHeader(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("id", pizza.getPizzaId());
        map.put("toppingIds", pizza.getToppings());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<BigDecimal> response = restTemplate.postForEntity(menuUrl + "/getPrice", entity, BigDecimal.class);

        return response.getBody();
    }

    /**
     * Checks if pizza contains any allergens' member is allergic to.
     *
     * @param pizza pizza to check for allergens
     * @param memberId check for allergens of this member
     * @param token authentication token
     * @return response message
     */
    public String containsAllergen(Pizza pizza, String memberId, String token) {
        HttpHeaders headers = makeHeader(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("id", pizza.getPizzaId());
        map.put("toppingIds", pizza.getToppings());
        map.put("memberId", memberId);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(menuUrl + "/containsAllergen", entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            return "";
        }

        return response.getBody();
    }

    /**
     * Checks if topping contains any allergens' member is allergic to.
     *
     * @param toppingId ID of the topping what we are checking for allergens
     * @param memberId ID of the member for who we are checking the allergies
     * @param token authentication token
     * @return a String of all allergens that topping contains for the member
     * @throws Exception when the topping is not valid
     */
    public String containsAllergenTopping(Long toppingId, String memberId, String token) throws Exception {
        HttpHeaders headers = makeHeader(token);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                menuUrl + "/containsAllergenTopping/" + toppingId + "/" + memberId,
                HttpMethod.POST,
                request,
                String.class
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Topping is not valid");
        }
        return response.getBody();
    }

    /**
     * Makes HttpHeaders with the selected token.
     *
     * @param token authentication token
     * @return HttpHeaders with appropriate Authorization set
     */
    private HttpHeaders makeHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
        return headers;
    }
}
