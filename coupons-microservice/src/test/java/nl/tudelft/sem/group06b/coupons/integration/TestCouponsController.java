package nl.tudelft.sem.group06b.coupons.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import nl.tudelft.sem.group06b.coupons.authentication.AuthManager;
import nl.tudelft.sem.group06b.coupons.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.model.ApplyCouponsRequestModel;
import nl.tudelft.sem.group06b.coupons.model.Pizza;
import nl.tudelft.sem.group06b.coupons.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        when(mockAuthenticationManager.getMemberId()).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken(anyString())).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.getAuthoritiesFromToken(anyString())).thenReturn(new SimpleGrantedAuthority("customer"));

        when(couponRepository.findAllById(List.of("1", "2", "3"))).thenReturn(List.of(
                new Coupon("1", CouponType.DISCOUNT, 0.5,
                        Date.from(Instant.now().plusSeconds(30)), new HashSet<>()),
                new Coupon("2", CouponType.ONE_OFF, 0.5,
                        Date.from(Instant.now().plusSeconds(30)), new HashSet<>()),
                new Coupon("3", CouponType.DISCOUNT, 0.4,
                        Date.from(Instant.now().plusSeconds(30)), new HashSet<>())
        ));

        ApplyCouponsRequestModel applyCouponsRequestModel = new ApplyCouponsRequestModel();
        applyCouponsRequestModel.setCoupons(List.of("1", "2", "3"));
        applyCouponsRequestModel.setPizzas(List.of(
                new Pizza(1, List.of(1L), new BigDecimal("10.00")),
                new Pizza(2, List.of(2L), new BigDecimal("100.00")),
                new Pizza(3, List.of(), new BigDecimal("30.00"))
        ));
        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(post("/api/coupons/calculatePrice")
                .content(objectMapper.writeValueAsString(applyCouponsRequestModel))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token"));

        result.andExpect(status().isOk());

        //Get response as List<String>
        ApplyCouponsRequestModel response = objectMapper
                .readValue(result.andReturn().getResponse().getContentAsString(), ApplyCouponsRequestModel.class);

        //Check if the response is correct
        assert response.getPizzas().get(1).getPrice().equals(BigDecimal.ZERO);
        assert response.getCoupons().get(0).equals("2");
    }
}
