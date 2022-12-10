package nl.tudelft.sem.group06b.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public class Topping {
    private final @Getter String name;
    private final @Getter int id;
    private @Getter @Setter List<Allergy> allergies;
}
