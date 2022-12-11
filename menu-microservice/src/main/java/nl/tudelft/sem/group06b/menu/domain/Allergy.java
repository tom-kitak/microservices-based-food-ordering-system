package nl.tudelft.sem.group06b.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Allergy {
    /**
     * id for the allergy.
     */
    private @Getter int id;
    /**
     * name for the allergy.
     */
    private @Getter String name;
}
