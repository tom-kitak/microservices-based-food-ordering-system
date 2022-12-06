package nl.tudelft.sem.template.store.domain;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "stores")
@NoArgsConstructor
public class Store {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @JsonProperty("id")
    private String id;

    @Column(name = "title", nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(name = "title", nullable = false)
    @JsonProperty("location")
    private String location;

    /**
     * Stores information in a single store
     * @param id the ID of the store
     * @param name the name of the store
     * @param location the loation of the store
     */
    public Store(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    /**
     * Returns the name of the store
     * @return The name of the store
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the location of the store
     * @return The location of the store
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns ture if two stores are equal
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id.equals(store.id) && name.equals(store.name) && location.equals(store.location);
    }

    /**
     * Generate a hash code for an object
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }
}
