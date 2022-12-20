package nl.tudelft.sem.group06b.menu.unit;

import nl.tudelft.sem.group06b.menu.domain.Allergy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class AllergyTests {
    @Test
    public void equalsTest() {
        Allergy a = new Allergy(5L, "Gluten");
        Allergy b = new Allergy(5L, "Gluten");
        Allergy c = new Allergy(3L, "Gluten");
        Allergy d = new Allergy(5L, "Yttrium");
        Assertions.assertThat(a).isEqualTo(b);
        Assertions.assertThat(a).isNotEqualTo(c);
        Assertions.assertThat(a).isEqualTo(a);
        Assertions.assertThat(a).isNotEqualTo(d);
        Assertions.assertThat(a).isNotEqualTo(5);
        //Assertions.assertThat(a.hashCode()).isEqualTo(b.hashCode());
        //Assertions.assertThat(a.hashCode()).isNotEqualTo(c.hashCode());
    }
}
