package nl.tudelft.sem.group06b.order.unit;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.domain.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderTest {

    Order order1;

    Order order2;

    Pizza pizza1;

    Pizza pizza2;

    /**
     * sets up the tests.
     */
    @BeforeEach
    public void setUp() {
        this.pizza1 = new Pizza(23L, List.of(11L, 12L), new BigDecimal("11.29"));
        this.pizza2 = new Pizza(12L, List.of(11L), new BigDecimal("10.99"));
        this.order1 = new Order("randomUser", Status.ORDER_PLACED);
        this.order2 = new Order("randomUser2", Status.ORDER_PLACED);
        order1.setId(1L);
        order2.setId(2L);
    }

    @Test
    void calculateTotalPriceTest() {
        order1.setPizzas(List.of(this.pizza1, this.pizza2));
        order1.calculateTotalPrice();
        order2.setPizzas(List.of(this.pizza1));
        order2.calculateTotalPrice();
        Assertions.assertThat(order1.getPrice()).isEqualTo(new BigDecimal("22.28"));
        Assertions.assertThat(order1.getPrice()).isNotEqualTo(new BigDecimal("22.80"));
        Assertions.assertThat(order2.getPrice()).isEqualTo(new BigDecimal("11.29"));
    }

    @Test
    public void getIdTest() {
        Assertions.assertThat(order1.getId()).isEqualTo(1L);
    }

    @Test
    public void getStatusTest() {
        order1.setStatus(Status.ORDER_PLACED);
        Assertions.assertThat(order1.getStatus()).isEqualTo(Status.ORDER_PLACED);
        Assertions.assertThat(order1.getStatus()).isNotEqualTo(Status.ORDER_CANCELED);
    }

    @Test
    public void formatEmailTest() {
        order1.setStatus(Status.ORDER_PLACED);
        order1.setId(1L);
        order1.setSelectedTime("randomTime");
        order1.setPizzas(List.of(this.pizza1, this.pizza2));
        order1.calculateTotalPrice();
        order1.setAppliedCoupon("");
        Assertions.assertThat(order1.getStatus()).isEqualTo(Status.ORDER_PLACED);
        Assertions.assertThat(order1.getStatus()).isNotEqualTo(Status.ORDER_CANCELED);
        Assertions.assertThat(order1.formatEmail()).isEqualTo(
                "Order with an ID of 1 has been placed by randomUser, to be collected at randomTime\n"
                        + "Order contains the following pizzas:\n"
                + "[Pizza ID: 23, topping IDs: [11, 12], price: 11.29, Pizza ID: 12, topping IDs: [11], price: 10.99]\n"
                        + "No coupon was appliedFinal price of the order is 22.28");
    }

    @Test
    public void formatReceipt() {
        order1.setStatus(Status.ORDER_PLACED);
        order1.setId(1L);
        order1.setSelectedTime("randomTime");
        order1.setPizzas(List.of(this.pizza1, this.pizza2));
        order1.calculateTotalPrice();
        order1.setAppliedCoupon("");
        Assertions.assertThat(order1.getStatus()).isEqualTo(Status.ORDER_PLACED);
        Assertions.assertThat(order1.getStatus()).isNotEqualTo(Status.ORDER_CANCELED);
        Assertions.assertThat(order1.formatReceipt()).isEqualTo("Order ID: 1\n"
                + "Date: randomTime\n"
                + "Location: null\n"
                + "Pizzas: [Pizza ID: 23, topping IDs: [11, 12], "
                + "price: 11.29, Pizza ID: 12, topping IDs: [11], price: 10.99]\n"
                + "Final price: 22.28No coupon was applied");
    }

    @Test
    public void equalsTest() {
        order1.setStatus(Status.ORDER_PLACED);
        order1.setId(1L);
        order1.setSelectedTime("randomTime");
        order1.setPizzas(List.of(this.pizza1, this.pizza2));
        order1.calculateTotalPrice();
        order1.setAppliedCoupon("");
        Order orderEquals = new Order("randomUser", Status.ORDER_PLACED);
        orderEquals.setStatus(Status.ORDER_PLACED);
        orderEquals.setId(1L);
        orderEquals.setSelectedTime("randomTime");
        orderEquals.setPizzas(List.of(this.pizza1, this.pizza2));
        orderEquals.calculateTotalPrice();
        orderEquals.setAppliedCoupon("");
        Assertions.assertThat(order1.equals(orderEquals)).isTrue();
        Order orderTwoEquals = new Order("randomUser", Status.ORDER_PLACED);
        orderTwoEquals.setId(1L);
        Order orderThreeEquals = new Order("randomUser", Status.ORDER_PLACED);
        Assertions.assertThat(order1.equals(orderTwoEquals)).isTrue();
        Assertions.assertThat(order1.equals(orderThreeEquals)).isFalse();
        Assertions.assertThat(order1.equals(5)).isFalse();
        Assertions.assertThat(order1.equals(order1)).isTrue();
    }

    @Test
    public void gettersAndSettersTest() {
        /*public Order(String memberId, List<Pizza> pizzas, String selectedTime,
                Status status, BigDecimal price, Long storeId,
                String location, String appliedCoupon, Set<String> coupons) {*/
        HashSet<String> coupons = new HashSet<>();
        coupons.add("String");
        Order order3 = new Order("memberId", List.of(this.pizza1), "time", Status.ORDER_PLACED,
                new BigDecimal("24.99"), 12L, "location", "", coupons);
        order3.setId(5L);
        Order order4 = new Order("memberId", List.of(this.pizza1), "time", Status.ORDER_PLACED,
                new BigDecimal("24.99"), 12L, "location", "", coupons);
        order4.setId(5L);
        Assertions.assertThat(order3.equals(order4)).isTrue();
    }
}
