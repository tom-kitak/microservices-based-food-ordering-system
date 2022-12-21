package nl.tudelft.sem.group06b.menu.models;

import java.util.List;
import lombok.Data;

/**
 * model for the isValid request.
 */
@Data
public class ValidModel {
    Long id;
    List<Long> toppingIds;
}
