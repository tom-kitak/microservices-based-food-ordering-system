package nl.tudelft.sem.group06b.store.domain.email;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ValidateManagerService {

    private final transient StoreRepository storeRepository;

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
