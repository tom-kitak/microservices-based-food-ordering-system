package nl.tudelft.sem.group06b.menu.models;

import java.util.List;
import lombok.Data;

/**
 * Request body for containsAllergenModel.
 */
@Data
public class ContainsAllergenModel {
    Long id;

    List<Long> toppingIds;

    String memberId;
}
