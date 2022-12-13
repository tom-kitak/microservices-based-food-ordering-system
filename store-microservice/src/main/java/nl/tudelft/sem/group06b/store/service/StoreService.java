package nl.tudelft.sem.group06b.store.service;

import java.util.Optional;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.NoSuchStoreException;
import nl.tudelft.sem.group06b.store.domain.Store;
import nl.tudelft.sem.group06b.store.domain.StoreAlreadyExistException;
import org.springframework.stereotype.Service;


@Service
public class StoreService {

    private final transient StoreRepository storeRepository;

    /**
     * Instantiate a store service.
     *
     * @param storeRepository Input repository.
     */
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * Add a store to the existing stores.
     *
     * @param name The store name.
     * @param location The store location.
     * @return Added store.
     * @throws Exception If the store already exists at the given location.
     */
    public Store addStore(String name, Location location) throws Exception {
        if (checkLocationIsUnique(location)) {
            Store store = new Store(name, location);
            storeRepository.save(store);
            storeRepository.flush();
            return store;
        }
        throw new StoreAlreadyExistException(location);
    }

    /**
     * Removes a store from the existing stores.
     *
     * @param location The store location.
     * @return Removed store.
     * @throws Exception If the store does not exist at the given location.
     */
    public Store removeStore(Location location) throws Exception {
        if (checkLocationIsUnique(location)) {
            Optional<Store> store = storeRepository.findByStoreLocation(location);
            store.ifPresent(storeRepository::delete);
            storeRepository.flush();
        }
        throw new NoSuchStoreException(location);
    }

    /**
     * Checks if the given location is unique in the database.
     *
     * @param location Input location.
     * @return False is the given location is already in database.
     */
    public boolean checkLocationIsUnique(Location location) {
        return !storeRepository.existsByStoreLocation(location);
    }
}
