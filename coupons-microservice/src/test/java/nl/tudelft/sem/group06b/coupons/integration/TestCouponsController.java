package nl.tudelft.sem.group06b.coupons.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import nl.tudelft.sem.group06b.coupons.authentication.AuthManager;
import nl.tudelft.sem.group06b.coupons.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockCouponRepository", "mockTokenVerifier", "mockAuthenticationManager"})
@AutoConfigureMockMvc
public class TestCouponsController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient CouponRepository couponRepository;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Test
    public void testCalculateMinPrice() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("ExampleUser");

        when(couponRepository.findAllById(List.of("1", "2", "3"))).thenReturn(List.of(
                new Coupon("1", CouponType.DISCOUNT, 0.5),
                new Coupon("2", CouponType.ONE_OFF, 0.5),
                new Coupon("3", CouponType.DISCOUNT, 0.4)
        ));

        ResultActions result = mockMvc.perform(get("/calculateMinPrice?prices=100,20,10&coupons=1,2,3")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        result.andExpect(status().isOk());

        //Get response as List<String>
        List<String> response = List.of(result.andReturn().getResponse().getContentAsString().replace("[", "")
                .replace("]", "").replace("\"", "").split(","));

        //Check if the response is correct
        assert Double.valueOf(response.get(0)).equals(30D);
        assert response.get(1).equals("2");
    }
}
