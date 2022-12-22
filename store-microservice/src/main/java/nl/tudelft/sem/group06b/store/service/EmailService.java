package nl.tudelft.sem.group06b.store.service;

import java.util.List;
import nl.tudelft.sem.group06b.store.database.EmailRepository;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.Email;
import nl.tudelft.sem.group06b.store.domain.NoSuchEmailException;
import nl.tudelft.sem.group06b.store.domain.NoSuchStoreException;
import nl.tudelft.sem.group06b.store.domain.Store;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    private final transient EmailRepository emailRepository;
    private final transient StoreRepository storeRepository;

    /**
     * Instantiate a email service.
     *
     * @param emailRepository The email repo.
     * @param storeRepository The store repo.
     */
    public EmailService(EmailRepository emailRepository, StoreRepository storeRepository) {
        this.emailRepository = emailRepository;
        this.storeRepository = storeRepository;
    }

    public List<Email> queryAllEmails() {
        return emailRepository.findAll();
    }

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
}
