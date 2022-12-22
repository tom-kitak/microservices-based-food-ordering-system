package nl.tudelft.sem.group06b.order.service.editing.utils;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.order.domain.Allergen;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MenuConnectionHelper {

    private final transient String MENU_URL = "http://localhost:8086/api/menu";

    private final transient RestTemplate restTemplate;

    private boolean validatePizza(Pizza pizza, String token) throws Exception {
        HttpHeaders headers = makeHeader(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("id", pizza.getPizzaId());
        map.put("toppingIds", pizza.getToppings());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(MENU_URL + "/isValid", entity, Boolean.class);

        return (response.getStatusCode() == HttpStatus.OK && response.getBody() != false);
    }

    private Collection<Allergen> checkAllergens(String token, Pizza pizza) {
        HttpHeaders headers = makeHeader(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("id", pizza.getPizzaId());
        map.put("toppingIds", pizza.getToppings());
        map.put("memberId", memberId);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(MENU_URL + "/containsAllergen", entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            return null;
        }

        return new ArrayList<>();
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
