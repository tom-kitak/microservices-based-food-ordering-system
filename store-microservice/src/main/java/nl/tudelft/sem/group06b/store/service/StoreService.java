package nl.tudelft.sem.group06b.store.service;

import java.util.ArrayList;
import java.util.List;
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
     * Query all stores from the database.
     *
     * @return A list of stores.
     */
    public List<Store> queryAllStores() {
        return storeRepository.findAll();
    }

    /**
     * Add a store to the existing stores.
     *
     * @param name The store name.
     * @param location The store location.
     * @throws Exception If the store already exists at the given location.
     */
    public void addStore(String name, Location location) throws Exception {
        if (checkLocationIsUnique(location)) {
            Store store = new Store(name, location, new ArrayList<>());
            storeRepository.save(store);
            storeRepository.flush();
        } else {
            throw new StoreAlreadyExistException(location);
        }
    }

    /**
     * Removes a store from the existing stores.
     *
     * @param storeId The store id.
     * @throws Exception If the store does not exist at the given location.
     */
    public void removeStore(Long storeId) throws Exception {
        Store store = storeRepository.findById(storeId).orElseThrow(NoSuchStoreException::new);
        storeRepository.delete(store);
        storeRepository.flush();
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

    /**
     * Checks if the given store location is valid.
     *
     * @param location The input location.
     * @return True if the input location is valid, false otherwise.
     */
    public boolean validateStoreLocation(Location location) {
        return storeRepository.existsByStoreLocation(location);
    }
}
