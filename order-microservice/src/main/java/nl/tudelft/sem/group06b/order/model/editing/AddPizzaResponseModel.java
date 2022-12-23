package nl.tudelft.sem.group06b.order.model.editing;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.Allergen;

@Data
@AllArgsConstructor
public class AddPizzaResponseModel {
    Collection<Allergen> allergens;
}
