package nl.tudelft.sem.group06b.order.communication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreCommunicationTest {


    @Mock
    private RestTemplate restTemplateMock;

    private final transient String storeUrl = "http://localhost:8084/api/stores";

    @Test
    public void getStoreIdFromLocationTest() {
        String location = "location";
        String token = "token";

        HttpHeaders headerForValidation = new HttpHeaders();
        headerForValidation.set("Authorization", String.format("Bearer %s", token));
        HttpEntity he = new HttpEntity(headerForValidation);

        StoreCommunication sc = new StoreCommunication(restTemplateMock);

        ResponseEntity<Long> response = new ResponseEntity(HttpStatus.BAD_REQUEST);

        when(restTemplateMock.exchange(
                storeUrl + "/getStoreId/" + location,
                HttpMethod.GET,
                he,
                Long.class
        )).thenReturn(response);

        Exception e = assertThrows(
                Exception.class,
                () -> sc.getStoreIdFromLocation(location, token),
                "Problem with location, please try again");
        assert e.getMessage().equals("Problem with location, please try again");
    }
}
