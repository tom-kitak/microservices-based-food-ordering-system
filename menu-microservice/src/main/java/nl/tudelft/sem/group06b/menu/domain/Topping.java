package nl.tudelft.sem.group06b.menu.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Entity
@Table(name = "Toppings")
@ToString
public class Topping implements Serializable {
    static final long serialVersionUID = 42L;
    /**
     * name of the topping.
     */
    private @Getter String name;
    /**
     * id of the allergy.
     */
    @Id
    @GeneratedValue
    private @Getter Long id;
    /**
     * list of the allergies it contains.
     */
    @OneToMany
    private @Getter @Setter List<Allergy> allergies;
}
