package nl.tudelft.sem.group06b.order.service.editing;

import java.util.Collection;
import nl.tudelft.sem.group06b.order.domain.Allergen;
import nl.tudelft.sem.group06b.order.domain.Pizza;

public interface OrderEditor {

    Collection<Allergen> addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception;

    void removePizza(Long orderId, Pizza pizza) throws Exception;

    Collection<Allergen> addTopping(String token, String memberId,
                                    Long orderId, Pizza pizza, Long toppingId) throws Exception;

    void removeTopping(Long orderId, Pizza pizza, Long toppingId) throws Exception;
}
