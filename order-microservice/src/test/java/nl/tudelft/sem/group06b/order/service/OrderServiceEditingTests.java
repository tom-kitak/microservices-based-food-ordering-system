package nl.tudelft.sem.group06b.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.model.Identification;
import nl.tudelft.sem.group06b.order.model.editing.OrderPizza;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import nl.tudelft.sem.group06b.order.service.editing.OrderEditor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class OrderServiceEditingTests {

    @MockBean
    private transient OrderRepository orderRepository;

    @MockBean
    private transient MenuCommunication menuCommunication;

    @MockBean
    private transient StoreCommunication storeCommunication;

    @MockBean
    private transient CouponCommunication couponCommunication;

    @Autowired
    private transient OrderEditor ordEdit;

    /**
     * sets up the tests.
     */
    @BeforeEach
    public void setUp() {
        Pizza pizza1 = new Pizza(23L, List.of(11L, 12L), new BigDecimal("11.29"));
        HashSet<String> coupons = new HashSet<>();
        coupons.add("String");
        when(orderRepository.getOne(5L))
                .thenReturn(new Order("memberId", List.of(pizza1), "time", Status.ORDER_PLACED,
                        new BigDecimal("24.99"), 12L, "location", "", coupons));
        when(orderRepository.getOne(6L))
                .thenReturn(null);
        List<Pizza> pizzas = new ArrayList<>();
        pizzas.add(pizza1);
        when(orderRepository.getOne(7L))
                .thenReturn(new Order("memberId", pizzas, "time", Status.ORDER_ONGOING,
                        new BigDecimal("24.99"), 12L, "location", "", coupons));
    }

    @Test
    public void addPizza() {
        Exception a = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza(null, null, null, null)
        );
        assertThat(a.getMessage()).isEqualTo("Invalid token");

        Exception b = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null", null, null, null)
        );
        assertThat(b.getMessage()).isEqualTo("Invalid order ID");

        Exception c = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null", null, 5L, null)
        );
        assertThat(c.getMessage()).isEqualTo("No active order with this ID");

        Exception d = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null", null, 6L, null)
        );
        assertThat(d.getMessage()).isEqualTo("No active order with this ID");

        Exception e = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null", null, 7L, null)
        );
        assertThat(e.getMessage()).isEqualTo("Invalid pizza");

        Exception f = assertThrows(
                Exception.class,
                () -> ordEdit.addPizza("null",
                        null,
                        7L,
                        new Pizza(25L, List.of(11L, 12L), new BigDecimal("11.29")))
        );
        assertThat(f.getMessage()).isEqualTo("Invalid member ID");
    }


    @Test
    public void removePizza() {
        Exception a = assertThrows(
                Exception.class,
                () -> ordEdit.removePizza(null, null)
        );
        assertThat(a.getMessage()).isEqualTo("Invalid order ID");

        Exception b = assertThrows(
                Exception.class,
                () -> ordEdit.removePizza(5L, null)
        );
        assertThat(b.getMessage()).isEqualTo("No active order with this ID");

        Exception c = assertThrows(
                Exception.class,
                () -> ordEdit.removePizza(6L, null)
        );
        assertThat(c.getMessage()).isEqualTo("No active order with this ID");

        Exception d = assertThrows(
                Exception.class,
                () -> ordEdit.removePizza(7L, null)
        );
        assertThat(d.getMessage()).isEqualTo("Invalid pizza");
    }

    @Test
    public void addTopping() {
        Exception a = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(new Identification(null, null), new OrderPizza(null, null), null)
        );
        assertThat(a.getMessage()).isEqualTo("Invalid order ID");

        Exception b = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(new Identification(null, null), new OrderPizza(5L, null), null)
        );
        assertThat(b.getMessage()).isEqualTo("No active order with this ID");

        Exception c = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(new Identification(null, null), new OrderPizza(6L, null), null)
        );
        assertThat(c.getMessage()).isEqualTo("No active order with this ID");

        Exception d = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(new Identification(null, null),
                        new OrderPizza(7L, null), null)
        );
        assertThat(d.getMessage()).isEqualTo("Invalid topping");

        Exception e = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(new Identification(null, null),
                        new OrderPizza(7L, null), 4L));
        assertThat(e.getMessage()).isEqualTo("Invalid pizza");

        Exception f = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(new Identification(null, null), new OrderPizza(7L,
                        new Pizza(25L, List.of(11L, 12L), new BigDecimal("11.29"))), 4L)
        );
        assertThat(f.getMessage()).isEqualTo("Invalid token");

        Exception g = assertThrows(
                Exception.class,
                () -> ordEdit.addTopping(new Identification("null", null), new OrderPizza(7L,
                        new Pizza(25L, List.of(11L, 12L), new BigDecimal("11.29"))), 4L)
        );
    }

    @Test
    public void removeTopping() {
        Exception a = assertThrows(
                Exception.class,
                () -> ordEdit.removeTopping(null, null, null)
        );
        assertThat(a.getMessage()).isEqualTo("Invalid order ID");

        Exception b = assertThrows(
                Exception.class,
                () -> ordEdit.removeTopping(5L, null, null)
        );
        assertThat(b.getMessage()).isEqualTo("No active order with this ID");

        Exception c = assertThrows(
                Exception.class,
                () -> ordEdit.removeTopping(6L, null, null)
        );
        assertThat(c.getMessage()).isEqualTo("No active order with this ID");

        Exception d = assertThrows(
                Exception.class,
                () -> ordEdit.removeTopping(7L, null, null)
        );
        assertThat(d.getMessage()).isEqualTo("Invalid topping");

        Exception e = assertThrows(
                Exception.class,
                () -> ordEdit.removeTopping(7L, null, 5L)
        );
        assertThat(e.getMessage()).isEqualTo("Invalid pizza");

        Exception f = assertThrows(Exception.class,
                () -> ordEdit.removeTopping(7L,
                        new Pizza(23L, List.of(11L, 12L), new BigDecimal("11.29")),
                        5L));
        assertThat(f.getClass()).isEqualTo(UnsupportedOperationException.class);

        Exception g = assertThrows(Exception.class,
                () -> ordEdit.removeTopping(7L,
                        new Pizza(25L, List.of(11L, 12L), new BigDecimal("11.29")),
                        5L));
        assertThat(g.getMessage()).isEqualTo("Invalid pizza");
    }
}
