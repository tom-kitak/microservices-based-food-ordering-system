package nl.tudelft.sem.group06b.coupons.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import nl.tudelft.sem.group06b.coupons.authentication.AuthManager;
import nl.tudelft.sem.group06b.coupons.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group06b.coupons.domain.Coupon;
import nl.tudelft.sem.group06b.coupons.domain.CouponType;
import nl.tudelft.sem.group06b.coupons.model.ApplyCouponsRequestModel;
import nl.tudelft.sem.group06b.coupons.model.NewCouponRequestModel;
import nl.tudelft.sem.group06b.coupons.model.Pizza;
import nl.tudelft.sem.group06b.coupons.service.CouponsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestCouponsController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @MockBean
    private transient AuthManager mockAuthManager;

    @MockBean
    private transient CouponsService mockCouponsService;

    @Test
    public void testAddCouponAsRegional() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("regional_manager"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        Date date = Date.from(Instant.now().plusSeconds(120));
        NewCouponRequestModel newCouponRequestModel = new NewCouponRequestModel("test69", "DISCOUNT", 0.2, date.getTime());
        ObjectMapper mapper = new ObjectMapper();

        ResultActions resultActions = mockMvc.perform(post("/api/coupons/addCoupon")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(newCouponRequestModel)));

        resultActions.andExpect(status().isOk());
        verify(mockCouponsService).addCoupon("test69", "DISCOUNT", 0.2, date);
    }

    @Test
    public void testAddCouponException() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("regional_manager"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        Date date = Date.from(Instant.now().plusSeconds(120));
        NewCouponRequestModel newCouponRequestModel = new NewCouponRequestModel("test69", "DISCOUNT", 0.2, date.getTime());
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new IllegalArgumentException("Invalid coupon type")).when(mockCouponsService).addCoupon("test69", "DISCOUNT", 0.2, date);

        ResultActions resultActions = mockMvc.perform(post("/api/coupons/addCoupon")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(newCouponRequestModel)));

        resultActions.andExpect(status().isBadRequest()).andExpect(status().reason("Invalid coupon type"));
        verify(mockCouponsService).addCoupon("test69", "DISCOUNT", 0.2, date);
    }

    @Test
    public void testAddCouponAsCustomer() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("customer"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        Date date = Date.from(Instant.now().plusSeconds(120));
        NewCouponRequestModel newCouponRequestModel = new NewCouponRequestModel("test69", "DISCOUNT", 0.2, date.getTime());
        ObjectMapper mapper = new ObjectMapper();

        ResultActions resultActions = mockMvc.perform(post("/api/coupons/addCoupon")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(newCouponRequestModel)));

        resultActions.andExpect(status().isUnauthorized()).andExpect(status().reason("You are not authorized to add coupons"));
        verify(mockCouponsService, never()).addCoupon("test69", "DISCOUNT", 0.2, date);
    }

    @Test
    public void testRemoveCouponAsRegional() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("regional_manager"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        ResultActions resultActions = mockMvc.perform(delete("/api/coupons/removeCoupon/test69")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        resultActions.andExpect(status().isOk());
        verify(mockCouponsService).removeCoupon("test69");
    }

    @Test
    public void testRemoveCouponException() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("regional_manager"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        doThrow(new IllegalArgumentException("Coupon does not exist")).when(mockCouponsService).removeCoupon("test69");

        ResultActions resultActions = mockMvc.perform(delete("/api/coupons/removeCoupon/test69")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        resultActions.andExpect(status().isBadRequest()).andExpect(status().reason("Coupon does not exist"));
        verify(mockCouponsService).removeCoupon("test69");
    }

    @Test
    public void testRemoveCouponAsCustomer() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("customer"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        ResultActions resultActions = mockMvc.perform(delete("/api/coupons/removeCoupon/test69")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        resultActions.andExpect(status().isUnauthorized()).andExpect(status().reason("You are not authorized to remove coupons"));
        verify(mockCouponsService, never()).removeCoupon("test69");
    }

    @Test
    public void testGetAllCoupons() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("regional_manager"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        when(mockCouponsService.queryAllCoupons())
                .thenReturn(List.of(new Coupon(
                        "test69",
                        CouponType.DISCOUNT,
                        0.2,
                        Date.from(Instant.now().plusSeconds(120)), new HashSet<>())));

        ResultActions resultActions = mockMvc.perform(get("/api/coupons/getCoupons")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        resultActions.andExpect(status().isOk());

        ObjectMapper mapper = new ObjectMapper();
        List<Coupon> coupons = mapper
                .readValue(
                        resultActions.andReturn().getResponse().getContentAsString(),
                        mapper.getTypeFactory().constructCollectionType(List.class, Coupon.class));

        assertEquals(1, coupons.size());
        assertEquals("test69", coupons.get(0).getCode());
        verify(mockCouponsService, times(1)).queryAllCoupons();
    }

    @Test
    public void testGetAllCouponsAsCustomer() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("customer"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        ResultActions resultActions = mockMvc.perform(get("/api/coupons/getCoupons")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        resultActions.andExpect(status().isUnauthorized()).andExpect(status()
                .reason("You are not authorized to get all coupons"));
        verify(mockCouponsService, never()).queryAllCoupons();
    }

    @Test
    public void testCheckAvailabilityPresent() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("regional_manager"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        when(mockCouponsService.isCouponAvailable("died11", "user")).thenReturn(true);

        ResultActions resultActions = mockMvc.perform(get("/api/coupons/checkAvailability/died11")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        resultActions.andExpect(status().isOk());
        verify(mockCouponsService, times(1)).isCouponAvailable("died11", "user");
    }

    @Test
    public void testCheckAvailabilityAbsent() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("regional_manager"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        when(mockCouponsService.isCouponAvailable("died11", "user")).thenReturn(false);

        ResultActions resultActions = mockMvc.perform(get("/api/coupons/checkAvailability/died11")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        resultActions.andExpect(status().isBadRequest()).andExpect(status().reason("Coupon is not available"));
    }

    @Test
    public void testCalculatePriceCorrect() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("regional_manager"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        List<Pizza> pizzas = List.of(
                new Pizza(32L, List.of(), new BigDecimal("40.00")),
                new Pizza(31L, List.of(), new BigDecimal("40.00")),
                new Pizza(11L, List.of(), new BigDecimal("400.00")));
        ApplyCouponsRequestModel requestModel = new ApplyCouponsRequestModel(pizzas, List.of(
                "test",
                "test2",
                "test3"));

        List<Pizza> pizzas2 = List.of(
                new Pizza(32L, List.of(), new BigDecimal("40.00")),
                new Pizza(31L, List.of(), new BigDecimal("40.00")),
                new Pizza(11L, List.of(), BigDecimal.ZERO));
        ApplyCouponsRequestModel responseModel = new ApplyCouponsRequestModel(pizzas2, List.of(
                "test"));

        when(mockCouponsService.calculatePrice(requestModel)).thenReturn(responseModel);

        ObjectMapper mapper = new ObjectMapper();
        ResultActions resultActions = mockMvc.perform(post("/api/coupons/calculatePrice")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(requestModel)));

        resultActions.andExpect(status().isOk());
        ApplyCouponsRequestModel response = mapper
                .readValue(
                        resultActions.andReturn().getResponse().getContentAsString(),
                        ApplyCouponsRequestModel.class);

        assertEquals(responseModel, response);
        verify(mockCouponsService, times(1)).calculatePrice(requestModel);
        verify(mockCouponsService, times(1)).useCoupon("test", "user");
    }

    @Test
    public void testCalculatePriceException() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn(roles).when(mockAuthManager).getRoles();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(new SimpleGrantedAuthority("regional_manager"))
                .when(mockJwtTokenVerifier).getAuthoritiesFromToken("token");

        List<Pizza> pizzas = List.of(
                new Pizza(32L, List.of(), new BigDecimal("40.00")),
                new Pizza(31L, List.of(), new BigDecimal("40.00")),
                new Pizza(11L, List.of(), new BigDecimal("400.00")));
        ApplyCouponsRequestModel requestModel = new ApplyCouponsRequestModel(pizzas, List.of(
                "test",
                "test2",
                "test3"));
        ObjectMapper mapper = new ObjectMapper();

        when(mockCouponsService.calculatePrice(requestModel)).thenThrow(new IllegalArgumentException("No coupons found"));

        ResultActions resultActions = mockMvc.perform(post("/api/coupons/calculatePrice")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(requestModel)));

        resultActions.andExpect(status().isBadRequest()).andExpect(status().reason("No coupons found"));

        verify(mockCouponsService, times(1)).calculatePrice(requestModel);
    }
}
