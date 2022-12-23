package nl.tudelft.sem.group06b.order.unit;

import java.math.BigDecimal;
import java.util.List;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * tests pizzas.
 */
public class PizzaTests {

    Pizza pizza1;

    Pizza pizza2;

    Pizza pizza3;

    /**
     * sets up the tests.
     */
    @BeforeEach()
    public void setUp() {
        this.pizza1 = new Pizza(23L, List.of(11L, 12L), new BigDecimal("11.29"));

        this.pizza2 = new Pizza(24L, List.of(11L), new BigDecimal("11.29"));

        this.pizza3 = new Pizza(25L, List.of(), new BigDecimal("11.29"));
    }

    @Test
    public void getPriceTest() {
        Assertions.assertThat(this.pizza2.getPrice()).isEqualTo(new BigDecimal("11.29"));
    }

    @Test
    public void getPizzaIdTest() {
        Assertions.assertThat(this.pizza2.getPizzaId()).isEqualTo(24L);
    }

    @Test
    public void getToppingsTest() {
        Assertions.assertThat(this.pizza2.getToppings()).hasSameElementsAs(List.of(11L));
        Assertions.assertThat(this.pizza3.getToppings()).hasSameElementsAs(List.of());
        this.pizza3.setToppings(List.of(14L));
        Assertions.assertThat(this.pizza3.getToppings()).hasSameElementsAs(List.of(14L));
        Assertions.assertThat(this.pizza1.getToppings()).hasSameElementsAs(List.of(11L, 12L));
    }

    @Test
    public void toStringTest() {
        Assertions.assertThat(this.pizza1.toString()).isEqualTo("Pizza ID: 23, topping IDs: [11, 12], price: 11.29");
    }

    @Test
    public void equals() {
        Assertions.assertThat(pizza1.equals(pizza2)).isFalse();
        Assertions.assertThat(pizza2.equals(pizza3)).isFalse();
        Assertions.assertThat(pizza1.equals(5)).isFalse();
        Pizza pizzaTest = new Pizza(23L, List.of(13L, 12L), new BigDecimal("11.29"));
        Assertions.assertThat(pizza1.equals(pizzaTest)).isFalse();
        Pizza pizzaTest2 = new Pizza(23L, List.of(11L, 12L), new BigDecimal("11.27"));
        Assertions.assertThat(pizza1.equals(pizzaTest2)).isFalse();
        Pizza pizzaTest3 = new Pizza(23L, List.of(11L, 12L), new BigDecimal("11.29"));
        Assertions.assertThat(pizza1.equals(pizzaTest3)).isTrue();
        Assertions.assertThat(pizza1.equals(pizza1)).isTrue();
    }
}
