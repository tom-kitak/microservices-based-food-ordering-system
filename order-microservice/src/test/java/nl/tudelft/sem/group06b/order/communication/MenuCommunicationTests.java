package nl.tudelft.sem.group06b.order.communication;

import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class MenuCommunicationTests {

    @MockBean
    private RestTemplate restTemplate;

    public final Pizza pizza = new Pizza(2L, List.of(3L), new BigDecimal("10.99"));

    @Test
    public void validatePizzaTest() {

        doReturn(ResponseEntity.ok(true)).when(restTemplate).postForEntity(
                Mockito.anyString(), Mockito.<HttpEntity<?>>any(), Mockito.<Class<Map>>any());

        MenuCommunication mc = new MenuCommunication(restTemplate);

        Assertions.assertThatNoException().isThrownBy(() -> mc.validatePizza(pizza, "token"));

    }

}
