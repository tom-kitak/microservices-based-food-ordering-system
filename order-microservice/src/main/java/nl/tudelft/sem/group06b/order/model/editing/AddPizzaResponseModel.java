package nl.tudelft.sem.group06b.order.model.editing;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.Allergen;

import java.util.Collection;

@Data
@AllArgsConstructor
public class AddPizzaResponseModel {
    Collection<Allergen> allergens;
}
