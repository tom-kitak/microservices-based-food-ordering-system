package nl.tudelft.sem.group06b.order.communication;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StoreCommunication {

    private final transient String storeUrl = "http://localhost:8084/api/stores";
    private final transient String storeEmailUrl = "http://localhost:8084/api/email";

    private final transient RestTemplate restTemplate;

    public StoreCommunication() {
        this.restTemplate = new RestTemplate();
    }

    public StoreCommunication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Checks if the location is valid.
     *
     * @param location location to validate
     * @param token authentication token
     * @throws Exception if the location or response is not valid
     */
    public boolean validateLocation(String location, String token) {

        ResponseEntity<Boolean> responseValidation = restTemplate.exchange(
                storeUrl + "/validateLocation/" + location,
                HttpMethod.GET,
                createGetRequest(token),
                Boolean.class
        );
        return responseValidation.getBody();
    }



    /**
     * Checks if the user is a manager and updates their role.
     *
     * @param manager memberId of the user
     * @param token authentication token
     * @throws Exception if the location or response is not valid
     */
    public boolean validateManager(String manager, String token) {
        ResponseEntity<Boolean> responseValidation = restTemplate.exchange(
                storeUrl + "/validateManager/" + manager,
                HttpMethod.GET,
                createGetRequest(token),
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
        ResponseEntity<Long> responseStoreId = restTemplate.exchange(
                storeUrl + "/getStoreId/" + location,
                HttpMethod.GET,
                createGetRequest(token),
                Long.class
        );
        if (responseStoreId.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new Exception("Problem with location, please try again");
        }
        return responseStoreId.getBody();
    }

    /**
     * Gets store ID from the manager memberId provided.
     *
     * @param manager memberId of store manager
     * @param token authentication token
     * @return store ID of the location
     * @throws Exception if invalid response
     */
    public Long getStoreIdFromManager(String manager, String token) throws Exception {
        ResponseEntity<Long> responseStoreId = restTemplate.exchange(
                storeUrl + "/getStoreIdManager/" + manager,
                HttpMethod.GET,
                createGetRequest(token),
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
            return;
        }
        System.out.println("Problem with sending an email");
    }

    /**
     * Creates GET request.
     *
     * @param token authentication token
     */
    public HttpEntity createGetRequest(String token) {
        HttpHeaders headerForValidation = new HttpHeaders();
        headerForValidation.set("Authorization", String.format("Bearer %s", token));
        return new HttpEntity(headerForValidation);
    }

}
