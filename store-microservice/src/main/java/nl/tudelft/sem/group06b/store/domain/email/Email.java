package nl.tudelft.sem.group06b.store.domain.email;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group06b.store.domain.store.Store;


@Entity
@Table(name = "INBOX")
@NoArgsConstructor
public class Email {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 20000)
    private String dummyEmail;

    @ManyToOne()
    @JoinColumn(name = "store_id")
    private Store store;

    /**
     * Stores information in a single email.
     *
     * @param dummyEmail The dummy email.
     * @param store The store.
     */
    public Email(String dummyEmail, Store store) {
        this.dummyEmail = dummyEmail;
        this.store = store;
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
     * Gets the dummy email.
     *
     * @return The dummy email.
     */
    public String getDummyEmail() {
        return dummyEmail;
    }

    /**
     * Gets the store.
     *
     * @return The store.
     */
    public Store getStore() {
        return store;
    }

    /**
     * Sets the store.
     *
     * @param store The store.
     */
    public void setStore(Store store) {
        this.store = store;
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
        Email email = (Email) o;
        return Objects.equals(id, email.id)
                && Objects.equals(dummyEmail, email.dummyEmail)
                && Objects.equals(store, email.store);
    }

    /**
     * Generate a hash code for an object.
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, dummyEmail, store);
    }
}
