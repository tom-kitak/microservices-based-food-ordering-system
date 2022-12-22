package nl.tudelft.sem.group06b.order.communication;

import nl.tudelft.sem.group06b.order.domain.Pizza;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class MenuCommunication {

    private final transient String menuUrl = "http://localhost:8086/api/menu";

    private final transient RestTemplate restTemplate;

    public MenuCommunication() {
        this.restTemplate = new RestTemplate();
    }

    public void validatePizza(Pizza pizza, String token) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
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

    public String containsAllergen(Pizza pizza, String memberId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
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
}
