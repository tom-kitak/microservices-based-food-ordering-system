package nl.tudelft.sem.group06b.store.domain.email;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.store.database.EmailRepository;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.store.NoSuchStoreException;
import nl.tudelft.sem.group06b.store.domain.store.Store;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProcessEmailService {

    private final transient EmailRepository emailRepository;
    private final transient StoreRepository storeRepository;


    /**
     * Receive a dummy email to a specific store.
     *
     * @param email The email.
     * @param storeId The store id.
     * @throws Exception If the store does not exist.
     */
    public void receiveEmail(String email, Long storeId) throws Exception {
        Store store = storeRepository.findById(storeId).orElseThrow(NoSuchStoreException::new);
        Email dummyEmail = new Email(email, store);
        emailRepository.save(dummyEmail);
        emailRepository.flush();
    }

    /**
     * Delete a dummy email in the database.
     *
     * @param emailId The email id.
     * @throws Exception If the email does not exist.
     */
    public void deleteEmail(Long emailId) throws Exception {
        Email dummyEmail = emailRepository.findById(emailId).orElseThrow(NoSuchEmailException::new);
        emailRepository.delete(dummyEmail);
        emailRepository.flush();
    }
}

