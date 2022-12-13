package nl.tudelft.sem.group06b.store.domain;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {

    static final long serialVersionUID = -4238572935769182641L;

    private String address;

    /**
     * Instantiates a new location.
     *
     * @param address the address of the store
     *
     */
    public Location(String address) {
        this.address = address;
    }

    /**
     * Gets the address of the store.
     *
     * @return The address of the store.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the store.
     *
     * @param address The input address.
     */
    public void setAddress(String address) {
        this.address = address;
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
