package nl.tudelft.sem.group06b.order.service.editing;

import nl.tudelft.sem.group06b.order.domain.AllergenResponse;
import nl.tudelft.sem.group06b.order.domain.Pizza;

public interface OrderEditor {

    AllergenResponse addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception;

    void removePizza(Long orderId, Pizza pizza) throws Exception;

    AllergenResponse addTopping(String token, Long orderId, Long toppingId, Pizza pizza, String memberId) throws Exception;

    void removeTopping(Long orderId, Long toppingId, Pizza pizza) throws Exception;
}
