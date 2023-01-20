package nl.tudelft.sem.group06b.order.Communication;


import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MenuCommunicationTests {

    @Mock
    private RestTemplate restTemplate;

    private final transient String menuUrl = "http://localhost:8086/api/menu";

    @InjectMocks
    MenuCommunication mc;

    public final Pizza pizza = new Pizza(2L, List.of(3L), new BigDecimal("10.99"));
    @Test
    public void validatePizzaTest() {

        HttpHeaders headers = makeHeader("token");
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate = Mockito.mock(RestTemplate.class);

        Mockito.when(restTemplate.exchange(
                        Mockito.anyString(), Mockito.<HttpMethod>any(), Mockito.<HttpEntity<?>>any(), Mockito.<Class<Map>>any())).
                thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        mc = new MenuCommunication(restTemplate);

        try {
            mc.validatePizza(pizza, "token");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

/*    public HttpEntity createPostRequest(String token, Pizza p) {
        HttpHeaders headerForValidation = new HttpHeaders();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("Authorization", String.format("Bearer %s", token));
        hm.put("id", p.getPizzaId().toString());
        headerForValidation.setAll(hm);
        return new HttpEntity(headerForValidation);
    }*/

    private HttpHeaders makeHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
        return headers;
    }

}
