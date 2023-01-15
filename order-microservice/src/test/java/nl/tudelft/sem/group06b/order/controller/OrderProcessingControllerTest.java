package nl.tudelft.sem.group06b.order.controller;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.Collections;
import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group06b.order.model.processor.CancelOrderRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.PlaceOrderRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.SetOrderLocationRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.SetOrderTimeRequestModel;
import nl.tudelft.sem.group06b.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderProcessingControllerTest {
    @MockBean
    private AuthManager mockAuthManager;

    @MockBean
    private OrderService mockOrderService;

    @MockBean
    private JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testStartOrder() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn("regional_manager").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(roles)
                .when(mockJwtTokenVerifier).getRoleFromToken("token");

        doReturn(1L).when(mockOrderService).startOrder("user");

        ResultActions result = mockMvc.perform(post("/api/order/start")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        assert result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString().equals("1");
    }

    @Test
    public void testStartOrderException() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn("regional_manager").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(roles)
                .when(mockJwtTokenVerifier).getRoleFromToken("token");

        doThrow(new Exception("Test exception")).when(mockOrderService).startOrder("user");

        ResultActions result = mockMvc.perform(post("/api/order/start")
                .header("Authorization", "Bearer token")
                .contentType("application/json"));

        result.andExpect(status().isExpectationFailed()).andExpect(status().reason("Test exception"));
    }

    @Test
    public void testSetOrderTimeCorrect() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn("regional_manager").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(roles)
                .when(mockJwtTokenVerifier).getRoleFromToken("token");

        SetOrderTimeRequestModel request = new SetOrderTimeRequestModel();
        request.setOrderId(1L);
        request.setOrderTime("12:00");
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(put("/api/order/change_time")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        result.andExpect(status().isOk());
        verify(mockOrderService).setOrderTime(1L, "12:00");

    }

    @Test
    public void testSetOrderTimeException() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn("regional_manager").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(roles)
                .when(mockJwtTokenVerifier).getRoleFromToken("token");

        doThrow(new Exception("Test exception")).when(mockOrderService).setOrderTime(1L, "12:00");

        SetOrderTimeRequestModel request = new SetOrderTimeRequestModel();
        request.setOrderId(1L);
        request.setOrderTime("12:00");
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(put("/api/order/change_time")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        result.andExpect(status().isExpectationFailed()).andExpect(status().reason("Test exception"));
        verify(mockOrderService).setOrderTime(1L, "12:00");
    }

    @Test
    public void testChangeLocationCorrect() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn("regional_manager").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        when(mockAuthManager.getToken()).thenReturn("token");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        SetOrderLocationRequestModel request = new SetOrderLocationRequestModel();
        request.setOrderId(1L);
        request.setLocation("legit hell");
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(put("/api/order/change_location")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        result.andExpect(status().isOk());
        verify(mockOrderService).setOrderLocation("token", 1L, "legit hell");
    }

    @Test
    public void testChangeLocationException() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn("regional_manager").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        when(mockAuthManager.getToken()).thenReturn("token");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        SetOrderLocationRequestModel request = new SetOrderLocationRequestModel();
        request.setOrderId(1L);
        request.setLocation("legit hell");
        ObjectMapper mapper = new ObjectMapper();
        doThrow(new Exception("test")).when(mockOrderService).setOrderLocation("token", 1L, "legit hell");

        ResultActions result = mockMvc.perform(put("/api/order/change_location")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        result.andExpect(status().isExpectationFailed()).andExpect(status().reason("test"));
        verify(mockOrderService).setOrderLocation("token", 1L, "legit hell");
    }

    @Test
    public void testPlaceOrderCorrect() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn("regional_manager").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        when(mockAuthManager.getToken()).thenReturn("token");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        when(mockOrderService.placeOrder("token", 1L)).thenReturn(null);

        PlaceOrderRequestModel request = new PlaceOrderRequestModel();
        request.setOrderId(1L);
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(post("/api/order/place")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        result.andExpect(status().isOk());
        assert (result.andReturn().getResponse().getContentAsString().equals("{\"order\":null}"));
    }

    @Test
    public void testPlaceOrderException() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn("regional_manager").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        when(mockAuthManager.getToken()).thenReturn("token");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        when(mockOrderService.placeOrder("token", 1L)).thenThrow(new Exception("test"));

        PlaceOrderRequestModel request = new PlaceOrderRequestModel();
        request.setOrderId(1L);
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(post("/api/order/place")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        result.andExpect(status().isExpectationFailed()).andExpect(status().reason("test"));
    }

    @Test
    public void testCancelOrderCorrect() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn("regional_manager").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        when(mockAuthManager.getToken()).thenReturn("token");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        CancelOrderRequestModel request = new CancelOrderRequestModel();
        request.setOrderId(1L);
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(post("/api/order/cancel")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        result.andExpect(status().isOk());
        verify(mockOrderService).cancelOrder("token", "user", "regional_manager", 1L);
    }

    @Test
    public void testCancelOrderException() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("regional_manager"));
        doReturn("regional_manager").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        when(mockAuthManager.getToken()).thenReturn("token");
        doReturn(roles).when(mockJwtTokenVerifier).getRoleFromToken("token");

        doThrow(new Exception("test")).when(mockOrderService).cancelOrder("token", "user", "regional_manager", 1L);

        CancelOrderRequestModel request = new CancelOrderRequestModel();
        request.setOrderId(1L);
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(post("/api/order/cancel")
                .header("Authorization", "Bearer token")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)));

        result.andExpect(status().isExpectationFailed()).andExpect(status().reason("test"));
        verify(mockOrderService).cancelOrder("token", "user", "regional_manager", 1L);
    }


}
