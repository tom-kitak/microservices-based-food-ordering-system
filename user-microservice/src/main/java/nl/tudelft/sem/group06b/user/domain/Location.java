package nl.tudelft.sem.group06b.user.domain;

import java.io.Serializable;

public class Location implements Serializable {
    /**
     * Class representing the location entity
     */
    private String location;

    /**
     * Instantiates a new location
     * @param location
     */
    public Location(String location){
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
