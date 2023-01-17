package nl.tudelft.sem.group06b.coupons.service.operations;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.coupons.model.Pizza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class BasketValidationServiceImpl implements BasketValidationService {

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

    public void throwEmptyBasket(boolean emptyBasket) {
        if (emptyBasket) {
            throw new IllegalArgumentException("The basket is empty");
        }
    }

    public void throwNoCoupons(boolean noCoupons) {
        if (noCoupons) {
            throw new IllegalArgumentException("No coupons found");
        }
    }
}
