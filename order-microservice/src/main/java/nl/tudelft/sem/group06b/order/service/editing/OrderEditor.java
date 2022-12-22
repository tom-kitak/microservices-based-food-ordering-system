package nl.tudelft.sem.group06b.order.service.editing;

import nl.tudelft.sem.group06b.order.domain.Allergen;
import nl.tudelft.sem.group06b.order.domain.Pizza;

import java.util.Collection;

public interface OrderEditor {

    Collection<Allergen> addPizza(String token, Long orderId, Pizza pizza);

    void removePizza(Long orderId, Pizza pizza);

    Collection<Allergen> addTopping(String token, Long orderId, Pizza pizza, Long toppingId);

    void removeTopping(Long orderId, Pizza pizza, Long toppingId);
}
