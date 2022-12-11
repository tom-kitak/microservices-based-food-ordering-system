package nl.tudelft.sem.group06b.menu.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Topping {
    /**
     * name of the topping.
     */
    private final @Getter String name;
    /**
     * id of the allergy.
     */
    private final @Getter int id;
    /**
     * list of the allergies it contains.
     */
    private @Getter @Setter List<Allergy> allergies;
}
