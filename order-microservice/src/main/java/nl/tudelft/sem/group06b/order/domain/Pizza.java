package nl.tudelft.sem.group06b.order.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Pizza implements Serializable {

    static final long serialVersionUID = -6470090944414208496L;

    private Long pizzaId;
    private List<Long> toppings;
    private BigDecimal price;

    /**
     * Instantiates a pizza.
     *
     * @param pizzaId ID of the pizza
     * @param toppings list of toppings on the pizza
     * @param price price of the pizza
     */
    public Pizza(Long pizzaId, List<Long> toppings, BigDecimal price) {
        this.pizzaId = pizzaId;
        this.toppings = toppings;
        this.price = price;
    }

    public Long getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(Long pizzaId) {
        this.pizzaId = pizzaId;
    }

    public List<Long> getToppings() {
        return toppings;
    }

    public void setToppings(List<Long> toppings) {
        this.toppings = toppings;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pizza)) {
            return false;
        }
        Pizza pizza = (Pizza) o;
        return Objects.equals(pizzaId, pizza.pizzaId)
                && Objects.equals(toppings, pizza.toppings)
                && Objects.equals(price, pizza.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pizzaId, toppings, price);
    }
}
