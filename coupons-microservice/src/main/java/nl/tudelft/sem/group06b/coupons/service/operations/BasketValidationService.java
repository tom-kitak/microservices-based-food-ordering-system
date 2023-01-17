package nl.tudelft.sem.group06b.coupons.service.operations;

import nl.tudelft.sem.group06b.coupons.model.Pizza;

import java.util.List;

public interface BasketValidationService {

    int minimumBasket(List<Pizza> pizzasWithDiscount, List<Pizza> pizzasWithOneOff);

    void throwEmptyBasket(boolean emptyBasket);

    void throwNoCoupons(boolean noCoupons);
}
