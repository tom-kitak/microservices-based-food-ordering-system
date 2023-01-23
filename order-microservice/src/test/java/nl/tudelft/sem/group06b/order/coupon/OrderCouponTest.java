package nl.tudelft.sem.group06b.order.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import nl.tudelft.sem.group06b.order.service.coupon.OrderCoupon;
import nl.tudelft.sem.group06b.order.service.coupon.OrderCouponImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@SpringBootTest
@AutoConfigureMockMvc
public class OrderCouponTest {
    @MockBean
    private AuthManager mockAuthManager;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private CouponCommunication couponCommunication;

    @MockBean
    private JwtTokenVerifier mockJwtTokenVerifier;

    OrderCoupon orderCoupon;
    Order order1;

    @BeforeEach
    void setup() {
        order1 = new Order();
        order1.setStatus(Status.ORDER_ONGOING);
        order1.setId(11L);
        order1.setSelectedTime("randomTime");
        Pizza pizza1 = new Pizza(23L, List.of(11L, 12L), new BigDecimal("11.29"));
        Pizza pizza2 = new Pizza(12L, List.of(11L), new BigDecimal("10.99"));
        order1.setPizzas(List.of(pizza1, pizza2));
        order1.calculateTotalPrice();
        order1.setAppliedCoupon("");
        String coupon = "extr10";
        String token = "token";

        when(orderRepository.getOne(11L)).thenReturn(order1);
        when(couponCommunication.validateCoupon(coupon, token)).thenReturn(true);

        orderCoupon = new OrderCouponImpl(orderRepository);
    }

    @Test
    public void addCouponTest() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        doReturn("customer").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(roles)
                .when(mockJwtTokenVerifier).getRoleFromToken("token");

        String coupon = "extr10";
        String token = "token";

        when(orderRepository.getOne(11L)).thenReturn(order1);
        when(couponCommunication.validateCoupon(coupon, token)).thenReturn(true);

        OrderCouponImpl orderCoupon = new OrderCouponImpl(orderRepository);

        Exception exception1 = assertThrows(Exception.class, () -> {
            orderCoupon.addCoupon(null, 11L, coupon);
        });
        assertEquals("Invalid token", exception1.getMessage());

        Exception exception2 = assertThrows(Exception.class, () -> {
            orderCoupon.addCoupon(token, 12L, coupon);
        });

        Exception exception3 = assertThrows(Exception.class, () -> {
            orderCoupon.addCoupon(token, 11L, null);
        });
        assertEquals("Please enter valid coupon", exception3.getMessage());

        Exception exception4 = assertThrows(Exception.class, () -> {
            orderCoupon.addCoupon(token, 11L, coupon);
        });

        Exception exception5 = assertThrows(Exception.class, () -> {
            orderCoupon.addCoupon(token, null, coupon);
        });
        assertEquals("Invalid order ID", exception5.getMessage());

        order1.setStatus(Status.ORDER_PLACED);
        Exception exception6 = assertThrows(Exception.class, () -> {
            orderCoupon.addCoupon("token", 11L, "extr10");
        });
        assertEquals("No active order with this ID", exception6.getMessage());

        when(couponCommunication.validateCoupon(coupon, token)).thenReturn(false);

    }

    @Test
    public void removeCouponTest() throws Exception {
        Collection<? extends GrantedAuthority> roles =
                Collections.singleton(new SimpleGrantedAuthority("customer"));
        doReturn("customer").when(mockAuthManager).getRole();
        when(mockAuthManager.getMemberId()).thenReturn("user");
        when(mockJwtTokenVerifier.validateToken("token")).thenReturn(true);
        when(mockJwtTokenVerifier.getMemberIdFromToken("token")).thenReturn("user");
        doReturn(roles)
                .when(mockJwtTokenVerifier).getRoleFromToken("token");
        String coupon = "extr10";
        String token = "token";

        when(orderRepository.getOne(11L)).thenReturn(order1);
        when(couponCommunication.validateCoupon(coupon, token)).thenReturn(true);

        OrderCouponImpl orderCoupon = new OrderCouponImpl(orderRepository);

        Exception exception1 = assertThrows(Exception.class, () -> {
            orderCoupon.removeCoupon(12L, coupon);
        });
        Exception exception2 = assertThrows(Exception.class, () -> {
            orderCoupon.removeCoupon(11L, null);
        });
        assertEquals("Please enter valid coupon", exception2.getMessage());

        Exception exception3 = assertThrows(Exception.class, () -> {
            orderCoupon.removeCoupon(null, coupon);
        });
        assertEquals("Invalid order ID", exception3.getMessage());

        order1.setStatus(Status.ORDER_PLACED);
        Exception exception4 = assertThrows(Exception.class, () -> {
            orderCoupon.removeCoupon(11L, "extr10");
        });
        assertEquals("No active order with this ID", exception4.getMessage());

        when(couponCommunication.validateCoupon(coupon, token)).thenReturn(false);
        Exception exception5 = assertThrows(Exception.class, () -> {
            orderCoupon.removeCoupon(11L, coupon);
        });

    }

}
