package nl.tudelft.sem.group06b.store.domain.store;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.Location;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class ProcessStoreService {

    private final transient StoreRepository storeRepository;

    /**
     * Add a store to the existing stores.
     *
     * @param name The store name.
     * @param location The store location.
     * @throws Exception If the store already exists at the given location.
     */
    public void addStore(String name, Location location, String manager) throws Exception {
        if (checkLocationIsUnique(location)) {
            Store store = new Store(name, location, new ArrayList<>(), manager);
            storeRepository.save(store);
            storeRepository.flush();
        } else {
            throw new StoreAlreadyExistException(location);
        }
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
}
