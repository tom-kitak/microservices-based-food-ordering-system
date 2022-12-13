package nl.tudelft.sem.group06b.store.domain;

public class StoreAlreadyExistException extends Exception {

    static final long serialVersionUID = -3245141242515331631L;

    /**
     * Indicates that a store is already existed on the given location.
     *
     * @param location The given location
     */
    public StoreAlreadyExistException(Location location) {
        super("Store already exists at " + location.getAddress());
    }
}
