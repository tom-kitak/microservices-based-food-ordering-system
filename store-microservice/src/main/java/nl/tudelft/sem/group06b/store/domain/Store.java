package nl.tudelft.sem.group06b.store.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "STORES")
@NoArgsConstructor
public class Store {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    /**
     * Stores information in a single store.
     *
     * @param id the ID of the store.
     * @param name the name of the store.
     * @param location the loation of the store.
     */
    public Store(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    /**
     * Returns the name of the store.
     *
     * @return The name of the store.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the location of the store.
     *
     * @return The location of the store.
     */
    public String getLocation() {
        return location;
    }

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
        Store store = (Store) o;
        return Objects.equals(id, store.id) && name.equals(store.name) && location.equals(store.location);
    }

    /**
     * Generate a hash code for an object.
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }
}
