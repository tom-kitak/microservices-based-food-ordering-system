package nl.tudelft.sem.group06b.menu.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Entity
@Table(name = "Pizzas")
public class Pizza implements Serializable {
    static final long serialVersionUID = 42L;
    /**
     * id for the pizza.
     */
    @Id
    public @Getter @Setter Long id;
    /**
     * list of toppings that the pizza has.
     */
    @OneToMany
    public @Getter @Setter List<Topping> toppings;

}
