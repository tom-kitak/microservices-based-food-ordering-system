package nl.tudelft.sem.group06b.order.service.editing;

import nl.tudelft.sem.group06b.order.domain.Allergens;
import nl.tudelft.sem.group06b.order.domain.Pizza;

public interface OrderEditor {

    Allergens addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception;

    void removePizza(Long orderId, Pizza pizza) throws Exception;

    Allergens addTopping(String token, String memberId,
                         Long orderId, Pizza pizza, Long toppingId) throws Exception;

    void removeTopping(Long orderId, Pizza pizza, Long toppingId) throws Exception;
}
