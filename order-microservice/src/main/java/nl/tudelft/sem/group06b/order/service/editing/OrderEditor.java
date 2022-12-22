package nl.tudelft.sem.group06b.order.service.editing;

import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.Allergen;
import nl.tudelft.sem.group06b.order.domain.Pizza;

public interface OrderEditor {

    Collection<Allergen> addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception;

    void removePizza();

    void addTopping();

    void removeTopping();
}
