package nl.tudelft.sem.group06b.store.domain.store;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.email.Email;


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
    private Location storeLocation;

    @Getter
    @Setter
    @Column(name = "manager", nullable = false, unique = true)
    private String manager;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Email> dummyEmails;

    /**
     * Stores information in a single store.
     *
     * @param name          the name of the store.
     * @param storeLocation the location of the store.
     */
    public Store(String name, Location storeLocation) {
        this.name = name;
        this.storeLocation = storeLocation;
    }

    /**
     * Stores information in a single store.
     *
     * @param name          the name of the store.
     * @param storeLocation the location of the store.
     * @param dummyEmails   the dummy emails of the store.
     */
    public Store(String name, Location storeLocation, List<Email> dummyEmails, String manager) {
        this.name = name;
        this.storeLocation = storeLocation;
        this.dummyEmails = dummyEmails;
        this.manager = manager;
    }

    /**
     * Gets the email id.
     *
     * @return The email id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the email id.
     *
     * @param id The email id.
     */
    public void setId(Long id) {
        this.id = id;
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
     * Sets the store name.
     *
     * @param name The input name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the location of the store.
     *
     * @return The location of the store.
     */
    public Location getStoreLocation() {
        return storeLocation;
    }

    /**
     * Sets the store location.
     *
     * @param storeLocation The input location.
     */
    public void setStoreLocation(Location storeLocation) {
        this.storeLocation = storeLocation;
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

        if (!Objects.equals(id, store.id)) {
            return false;
        }
        if (!Objects.equals(name, store.name)) {
            return false;
        }
        if (!Objects.equals(storeLocation, store.storeLocation)) {
            return false;
        }
        if (!Objects.equals(manager, store.manager)) {
            return false;
        }
        return Objects.equals(dummyEmails, store.dummyEmails);
    }

    /**
     * Generate a hash code for an object.
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, storeLocation);
    }
}
