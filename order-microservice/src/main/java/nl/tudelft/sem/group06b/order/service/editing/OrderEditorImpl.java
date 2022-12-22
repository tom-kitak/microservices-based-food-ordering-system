package nl.tudelft.sem.group06b.order.service.editing;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.domain.Allergen;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.service.editing.utils.MenuConnectionHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class OrderEditorImpl implements OrderEditor {

    private final transient MenuConnectionHelper menuConnectionHelper;

    @Override
    public Collection<Allergen> addPizza(String token, Long orderId, Pizza pizza) {

    }

    @Override
    public void removePizza(Long orderId, Pizza pizza) {

    }

    @Override
    public Collection<Allergen> addTopping(String token, Long orderId, Pizza pizza, Long toppingId) {
        return new ArrayList<>();
    }

    @Override
    public void removeTopping(Long orderId, Pizza pizza, Long toppingId) {

    }
}
