package nl.tudelft.sem.group06b.store.service;

import java.util.List;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.store.ProcessStoreService;
import nl.tudelft.sem.group06b.store.domain.store.QueryStoreService;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import nl.tudelft.sem.group06b.store.domain.store.ValidateStoreService;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class StoreService {

    private final transient ProcessStoreService processStoreService;
    private final transient QueryStoreService queryStoreService;
    private final transient ValidateStoreService validateStoreService;

    /**
     * Add a store to the existing stores.
     *
     * @param name The store name.
     * @param location The store location.
     * @throws Exception If the store already exists at the given location.
     */
    public void addStore(String name, Location location, String manager) throws Exception {
        processStoreService.addStore(name, location, manager);
    }

    /**
     * Removes a store from the existing stores.
     *
     * @param storeId The store id.
     * @throws Exception If the store does not exist at the given location.
     */
    public void removeStore(Long storeId) throws Exception {
        processStoreService.removeStore(storeId);
    }

    /**
     * Checks if the given store location is valid.
     *
     * @param location The input location.
     * @return True if the input location is valid, false otherwise.
     */
    public boolean validateStoreLocation(Location location) {
        return validateStoreService.validateStoreLocation(location);
    }

    /**
     * Checks if the given manager memberId is valid.
     *
     * @param manager The input location.
     * @return True if the input location is valid, false otherwise.
     */
    public boolean validateManager(String manager) {
        return validateStoreService.validateManager(manager);
    }

    /**
     * Query all stores from the database.
     *
     * @return A list of stores.
     */
    public List<Store> queryAllStores() {
        return queryStoreService.queryAllStores();
    }

    /**
     * Retrieves the id of the store.
     *
     * @param location The preferred location by user.
     * @return The store id.
     * @throws Exception If the given store location does not exist.
     */
    public Long retrieveStoreId(Location location) throws Exception {
        return queryStoreService.retrieveStoreId(location);
    }

    /**
     * Retrieves the id of the store.
     *
     * @param manager The memberId of the manager.
     * @return The store id.
     * @throws Exception If the given store location does not exist.
     */
    public Long retrieveStoreId(String manager) throws Exception {
        return queryStoreService.retrieveStoreId(manager);
    }


}

