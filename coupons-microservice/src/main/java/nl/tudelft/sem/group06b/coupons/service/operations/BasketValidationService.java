package nl.tudelft.sem.group06b.coupons.service.operations;

import java.util.List;
import nl.tudelft.sem.group06b.coupons.model.Pizza;

public interface BasketValidationService {

    /**
     * Given a basket, decided what is the optimal choice.
     *
     * @param pizzasWithDiscount the pizzas that can be applied a discount coupon
     * @param pizzasWithOneOff the pizzas that can be applied a oneOffCoupon
     * @return which type of coupon should the user apply
     */
    int minimumBasket(List<Pizza> pizzasWithDiscount, List<Pizza> pizzasWithOneOff);

    /**
     * Throws an exception in case the basket is empty.
     *
     * @param emptyBasket whether the basket is empty or not
     */
    void throwEmptyBasket(boolean emptyBasket);

    /**
     * Throws an exception in case there are no coupons.
     *
     * @param noCoupons whether there are coupons to apply or not
     */
    void throwNoCoupons(boolean noCoupons);
}
