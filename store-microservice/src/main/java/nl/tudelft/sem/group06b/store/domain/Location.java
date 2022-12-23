package nl.tudelft.sem.group06b.store.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location implements Serializable {

    static final long serialVersionUID = -4238572935769182641L;

    private String address;


    /**
     * Returns true if two stores are equal.
     *
     * @param o The object to compare with.
     * @return True if two stores are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return Objects.equals(address, location.address);
    }

    /**
     * Generate a hash code for an object.
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

}
