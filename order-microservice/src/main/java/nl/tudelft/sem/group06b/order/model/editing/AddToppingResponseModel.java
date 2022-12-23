package nl.tudelft.sem.group06b.order.model.editing;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.AllergenResponse;

@Data
@AllArgsConstructor
public class AddToppingResponseModel {
    AllergenResponse allergens;
}

