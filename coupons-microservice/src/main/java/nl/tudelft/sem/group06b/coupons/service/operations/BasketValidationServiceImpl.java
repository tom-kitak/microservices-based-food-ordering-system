package nl.tudelft.sem.group06b.coupons.service.operations;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.coupons.model.Pizza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class BasketValidationServiceImpl implements BasketValidationService {

    /**
     * Given a basket, decided what is the optimal choice.
     *
     * @param pizzasWithDiscount the pizzas that can be applied a discount coupon
     * @param pizzasWithOneOff the pizzas that can be applied a oneOffCoupon
     * @return which type of coupon should the user apply
     */
    public int minimumBasket(List<Pizza> pizzasWithDiscount, List<Pizza> pizzasWithOneOff) {
        BigDecimal discountPrice = pizzasWithDiscount.stream().map(Pizza::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal oneOffPrice = pizzasWithOneOff.stream().map(Pizza::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (discountPrice.compareTo(oneOffPrice) < 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Throws an exception in case the basket is empty.
     *
     * @param emptyBasket whether the basket is empty or not
     */
    public void throwEmptyBasket(boolean emptyBasket) {
        if (emptyBasket) {
            throw new IllegalArgumentException("The basket is empty");
        }
    }

    /**
     * Throws an exception in case there are no coupons.
     *
     * @param noCoupons whether there are coupons to apply or not
     */
    public void throwNoCoupons(boolean noCoupons) {
        if (noCoupons) {
            throw new IllegalArgumentException("No coupons found");
        }
    }
}
