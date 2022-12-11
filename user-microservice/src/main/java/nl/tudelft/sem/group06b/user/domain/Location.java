package nl.tudelft.sem.group06b.user.domain;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {

    static final long serialVersionUID = -3387516993124229945L;


    /**
     * Class representing the location entity.
     */
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
