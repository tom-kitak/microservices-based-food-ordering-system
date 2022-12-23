package nl.tudelft.sem.group06b.order.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import nl.tudelft.sem.group06b.order.service.editing.OrderEditorImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.ResourceAccessException;

public class OrderServiceEditingTests {
    OrderEditorImpl ordEdit;

    /**
     * sets up the tests.
     */
    @BeforeEach
    public void setUp() {
        OrderRepository or = mock(OrderRepository.class);
        /* MenuCommunication mc = mock(MenuCommunication.class);
        StoreCommunication sc = mock(StoreCommunication.class);
        CouponCommunication cc = mock(CouponCommunication.class);*/
        Pizza pizza1 = new Pizza(23L, List.of(11L, 12L), new BigDecimal("11.29"));
        HashSet<String> coupons = new HashSet<>();
        coupons.add("String");
        when(or.getOne(5L)).thenReturn(new Order("memberId", List.of(pizza1), "time", Status.ORDER_PLACED,
                new BigDecimal("24.99"), 12L, "location", "", coupons));
        when(or.getOne(6L)).thenReturn(null);
        when(or.getOne(7L)).thenReturn(new Order("memberId", List.of(pizza1), "time", Status.ORDER_ONGOING,
                new BigDecimal("24.99"), 12L, "location", "", coupons));
        this.ordEdit = new OrderEditorImpl(or);
    }

    @Test
    public void addPizza() {
        Exception a = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza(null, null, null, null)
        );
        Assertions.assertThat(a.getMessage()).isEqualTo("Invalid token");

        Exception b = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null", null, null, null)
        );
        Assertions.assertThat(b.getMessage()).isEqualTo("Invalid order ID");

        Exception c = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null", null, 5L, null)
        );
        Assertions.assertThat(c.getMessage()).isEqualTo("No active order with this ID");

        Exception d = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null", null, 6L, null)
        );
        Assertions.assertThat(d.getMessage()).isEqualTo("No active order with this ID");

        Exception e = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null", null, 7L, null)
        );
        Assertions.assertThat(e.getMessage()).isEqualTo("Invalid pizza");

        Exception f = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null",
                        null,
                        7L,
                        new Pizza(25L, List.of(11L, 12L), new BigDecimal("11.29")))
        );
        Assertions.assertThat(f.getMessage()).isEqualTo("Invalid member ID");

        Exception g = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null",
                        "memberId",
                        7L,
                        new Pizza(25L, List.of(11L, 12L), new BigDecimal("11.29")))
        );
        Assertions.assertThat(g.getClass()).isEqualTo(ResourceAccessException.class);
    }

    @Test
    public void removePizza() {
        Exception a = assertThrows(
                Exception.class,
                () -> ordEdit.removePizza(null, null)
        );
        Assertions.assertThat(a.getMessage()).isEqualTo("Invalid order ID");

        Exception b = assertThrows(
                Exception.class,
                () -> ordEdit.removePizza(5L, null)
        );
        Assertions.assertThat(b.getMessage()).isEqualTo("No active order with this ID");

        Exception c = assertThrows(
                Exception.class,
                () -> ordEdit.removePizza(6L, null)
        );
        Assertions.assertThat(c.getMessage()).isEqualTo("No active order with this ID");

        Exception d = assertThrows(
                Exception.class,
                () -> ordEdit.removePizza(7L, null)
        );
        Assertions.assertThat(d.getMessage()).isEqualTo("Invalid pizza");


        Exception e = assertThrows(Exception.class,
                () -> ordEdit.removePizza(7L,
                new Pizza(23L, List.of(11L, 12L),
                        new BigDecimal("11.29"))));
        Assertions.assertThat(e.getClass()).isEqualTo(UnsupportedOperationException.class);
    }

    @Test
    public void addTopping() {
        Exception a = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(null, null, null, null, null)
        );
        Assertions.assertThat(a.getMessage()).isEqualTo("Invalid order ID");

        Exception b = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(null, null, 5L, null, null)
        );
        Assertions.assertThat(b.getMessage()).isEqualTo("No active order with this ID");

        Exception c = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(null, null, 6L, null, null)
        );
        Assertions.assertThat(c.getMessage()).isEqualTo("No active order with this ID");

        Exception d = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(null, null, 7L, null, null)
        );
        Assertions.assertThat(d.getMessage()).isEqualTo("Invalid topping");

        Exception e = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(null, null, 7L,
                        null, 4L));
        Assertions.assertThat(e.getMessage()).isEqualTo("Invalid pizza");

        Exception f = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(null, null, 7L,
                        new Pizza(25L, List.of(11L, 12L), new BigDecimal("11.29")), 4L)
        );
        Assertions.assertThat(f.getMessage()).isEqualTo("Invalid token");

        Exception g = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping("null", null, 7L,
                        new Pizza(25L, List.of(11L, 12L), new BigDecimal("11.29")), 4L)
        );
        System.out.println(g.getMessage());
        Assertions.assertThat(g.getClass()).isEqualTo(ResourceAccessException.class);
    }

    @Test
    public void removeTopping() {
        Exception a = assertThrows(
                Exception.class,
                () -> ordEdit.removeTopping(null, null, null)
        );
        Assertions.assertThat(a.getMessage()).isEqualTo("Invalid order ID");

        Exception b = assertThrows(
                Exception.class,
                () -> ordEdit.removeTopping(5L, null, null)
        );
        Assertions.assertThat(b.getMessage()).isEqualTo("No active order with this ID");

        Exception c = assertThrows(
                Exception.class,
                () -> ordEdit.removeTopping(6L, null, null)
        );
        Assertions.assertThat(c.getMessage()).isEqualTo("No active order with this ID");

        Exception d = assertThrows(
                Exception.class,
                () -> ordEdit.removeTopping(7L, null, null)
        );
        Assertions.assertThat(d.getMessage()).isEqualTo("Invalid topping");

        Exception e = assertThrows(
                Exception.class,
                () -> ordEdit.removeTopping(7L, null, 5L)
        );
        Assertions.assertThat(e.getMessage()).isEqualTo("Invalid pizza");

        Exception f = assertThrows(Exception.class,
                () -> ordEdit.removeTopping(7L,
                                new Pizza(23L, List.of(11L, 12L), new BigDecimal("11.29")),
                                5L));
        Assertions.assertThat(f.getClass()).isEqualTo(UnsupportedOperationException.class);

        Exception g = assertThrows(Exception.class,
                () -> ordEdit.removeTopping(7L,
                        new Pizza(25L, List.of(11L, 12L), new BigDecimal("11.29")),
                        5L));
        Assertions.assertThat(g.getMessage()).isEqualTo("Invalid pizza");
    }
}
