package nl.tudelft.sem.group06b.store.database;

import java.util.Optional;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {


    /**
     * Check if a store already exists on the given location.
     *
     * @param location Input location
     * @return True if store already exist on the given location
     */
    boolean existsByStoreLocation(Location location);

    /**
     * Find a store by its location.
     *
     * @param location Input location.
     * @return An optional Store.
     */
    Optional<Store> findByStoreLocation(Location location);

    /**
     * Check if a store already exists with the given manager.
     *
     * @param manager memberId of the manager
     * @return True if store already exist on the given location
     */
    boolean existsByManager(String manager);

    /**
     * Find a store by its manager.
     *
     * @param manager memberId of the manager.
     * @return An optional Store.
     */
    Optional<Store> findByManager(String manager);
}
