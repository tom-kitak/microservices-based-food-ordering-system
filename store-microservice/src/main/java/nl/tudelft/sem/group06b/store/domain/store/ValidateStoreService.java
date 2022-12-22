package nl.tudelft.sem.group06b.store.domain.store;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.Location;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ValidateStoreService {

    private final transient StoreRepository storeRepository;

    /**
     * Checks if the given store location is valid.
     *
     * @param location The input location.
     * @return True if the input location is valid, false otherwise.
     */
    public boolean validateStoreLocation(Location location) {
        return storeRepository.existsByStoreLocation(location);
    }

    /**
     * Checks if the given manager memberId is valid.
     *
     * @param manager The input location.
     * @return True if the input location is valid, false otherwise.
     */
    public boolean validateManager(String manager) {
        return storeRepository.existsByManager(manager);
    }
}
