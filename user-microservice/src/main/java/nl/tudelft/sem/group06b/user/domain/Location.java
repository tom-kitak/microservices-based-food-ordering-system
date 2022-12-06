package nl.tudelft.sem.group06b.user.domain;

import java.io.Serializable;

public class Location implements Serializable {

    static final long serialVersionUID = -3387516993124229945L;

    /**
     * Class representing the location entity.
     */
    private transient String address;

    /**
     * Instantiates a new location.
     *
     * @param address the address of the store
     *
     */
    public Location(String address) {
        this.address = address;
    }

    public String getLocation() {
        return address;
    }
}
