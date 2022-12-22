package nl.tudelft.sem.group06b.order.communication;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class StoreCommunication {

    private final transient String storeUrl = "http://localhost:8084/api/stores";
    private final transient String storeEmailUrl = "http://localhost:8084/api/email";

    private final transient RestTemplate restTemplate;

    public StoreCommunication() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Checks if the location is valid.
     *
     * @param location location to validate
     * @param token authentication token
     * @throws Exception if the location or response is not valid
     */
    public boolean validateLocation(String location, String token) {
        HttpHeaders headerForValidation = new HttpHeaders();
        headerForValidation.set("Authorization", String.format("Bearer %s", token));
        HttpEntity requestValidation = new HttpEntity(headerForValidation);
        ResponseEntity<Boolean> responseValidation = restTemplate.exchange(
                storeUrl + "/validateLocation/" + location,
                HttpMethod.GET,
                requestValidation,
                Boolean.class
        );
        return responseValidation.getBody();
    }

    /**
     * Gets store ID from the location provided.
     *
     * @param location location to get the store ID from
     * @param token authentication token
     * @return store ID of the location
     * @throws Exception if invalid response
     */
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

    /**
     * Sends email to specified store.
     *
     * @param storeId store ID to send email to
     * @param email content to send
     * @param token authentication token
     */
    public void sendEmailToStore(Long storeId, String email, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("storeId", storeId);
        map.put("email", email);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(storeEmailUrl + "/sendEmail", entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody());
        }
        System.out.println("Problem with sending an email");
    }

}
