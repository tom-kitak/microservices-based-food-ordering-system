package nl.tudelft.sem.group06b.store.domain.email;

import java.util.List;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.store.database.EmailRepository;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.store.NoSuchStoreException;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class QueryEmailService {

    private final transient EmailRepository emailRepository;
    private final transient StoreRepository storeRepository;

    /**
     * Query all emails from the database.
     *
     * @return A list of emails.
     */
    public List<Email> queryAllEmails() {
        return emailRepository.findAll();
    }

    /**
     * Gets dummy emails from a specific store.
     *
     * @param storeId The store id.
     * @return A list of dummy emails.
     * @throws Exception If the store does not exist.
     */
    public List<Email> getEmailsFromStore(Long storeId) throws Exception {
        storeRepository.findById(storeId).orElseThrow(NoSuchStoreException::new);
        return emailRepository.retrieveEmailsByStoreId(storeId);
    }

    /**
     * Gets a manager from a specific store.
     *
     * @param storeId The store id.
     * @return The store manager.
     * @throws Exception If the store does not exist.
     */
    public String getManagerFromStore(Long storeId) throws Exception {
        Store store =  storeRepository.findById(storeId).orElseThrow(NoSuchStoreException::new);
        return store.getManager();
    }
}

