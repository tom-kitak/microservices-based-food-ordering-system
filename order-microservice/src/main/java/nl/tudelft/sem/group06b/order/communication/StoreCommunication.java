package nl.tudelft.sem.group06b.order.communication;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class StoreCommunication {

    private final transient String storeUrl = "http://localhost:8084/api/stores";

    private final transient RestTemplate restTemplate;

    public StoreCommunication() {
        this.restTemplate = new RestTemplate();
    }

    public void validateLocation(String location, String token) throws Exception {
        HttpHeaders headerForValidation = new HttpHeaders();
        headerForValidation.set("Authorization", String.format("Bearer %s", token));
        HttpEntity requestValidation = new HttpEntity(headerForValidation);
        ResponseEntity<Boolean> responseValidation = restTemplate.exchange(
                storeUrl + "/validateLocation/" + location,
                HttpMethod.GET,
                requestValidation,
                Boolean.class
        );
        if (responseValidation.getBody() == false) {
            throw new Exception("Location " + location + " is not valid");
        }
    }

    public Long getStoreIdFromLocation(String location, String token) throws Exception {
        HttpHeaders headerForStoreId = new HttpHeaders();
        headerForStoreId.set("Authorization", String.format("Bearer %s", token));
        HttpEntity requestStoreId = new HttpEntity(headerForStoreId);
        ResponseEntity<Long> responseStoreId = restTemplate.exchange(
                storeUrl + "/getStoreId/" + location,
                HttpMethod.GET,
                requestStoreId,
                Long.class
        );
        if (responseStoreId.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new Exception("Problem with location, please try again");
        }
        return responseStoreId.getBody();
    }
}
