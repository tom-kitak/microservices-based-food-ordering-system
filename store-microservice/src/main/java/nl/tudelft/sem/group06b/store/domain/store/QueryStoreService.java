package nl.tudelft.sem.group06b.store.domain.store;

import java.util.List;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.Location;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class QueryStoreService {

    private final transient StoreRepository storeRepository;

    /**
     * Query all stores from the database.
     *
     * @return A list of stores.
     */
    public List<Store> queryAllStores() {
        return storeRepository.findAll();
    }

    /**
     * Retrieves the id of the store.
     *
     * @param location The preffered location by user.
     * @return The store id.
     * @throws Exception If the given store location does not exist.
     */
    public Long retrieveStoreId(Location location) throws Exception {
        Store store = storeRepository.findByStoreLocation(location)
                .orElseThrow(NoSuchStoreException::new);
        return store.getId();
    }

    /**
     * Retrieves the id of the store.
     *
     * @param manager The memberId of the manager.
     * @return The store id.
     * @throws Exception If the given store location does not exist.
     */
    public Long retrieveStoreId(String manager) throws Exception {
        Store store = storeRepository.findByManager(manager)
                .orElseThrow(NoSuchManagerException::new);
        return store.getId();
    }
}
