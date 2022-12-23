package nl.tudelft.sem.group06b.store.service;

import java.util.List;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.store.domain.email.Email;
import nl.tudelft.sem.group06b.store.domain.email.ProcessEmailService;
import nl.tudelft.sem.group06b.store.domain.email.QueryEmailService;
import nl.tudelft.sem.group06b.store.domain.email.ValidateManagerService;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class EmailService {

    private final transient ProcessEmailService processEmailService;
    private final transient QueryEmailService queryEmailService;
    private final transient ValidateManagerService validateManagerService;


    /**
     * Query all emails from the database.
     *
     * @return A list of emails.
     */
    public List<Email> queryAllEmails() {
        return queryEmailService.queryAllEmails();
    }

    /**
     * Receive a dummy email to a specific store.
     *
     * @param email The email.
     * @param storeId The store id.
     * @throws Exception If the store does not exist.
     */
    public void receiveEmail(String email, Long storeId) throws Exception {
        processEmailService.receiveEmail(email, storeId);
    }

    /**
     * Delete a dummy email in the database.
     *
     * @param emailId The email id.
     * @throws Exception If the email does not exist.
     */
    public void deleteEmail(Long emailId) throws Exception {
        processEmailService.deleteEmail(emailId);
    }

    /**
     * Gets dummy emails from a specific store.
     *
     * @param storeId The store id.
     * @return A list of dummy emails.
     * @throws Exception If the store does not exist.
     */
    public List<Email> getEmailsFromStore(Long storeId) throws Exception {
        return queryEmailService.getEmailsFromStore(storeId);
    }

    /**
     * Checks if the given manager memberId is valid.
     *
     * @param manager The input location.
     * @return True if the input location is valid, false otherwise.
     */
    public boolean validateManager(String manager) {
        return validateManagerService.validateManager(manager);
    }

    /**
     * Gets a manager from a specific store.
     *
     * @param storeId The store id.
     * @return The store manager.
     * @throws Exception If the store does not exist.
     */
    public String getManagerFromStore(Long storeId) throws Exception {
        return queryEmailService.getManagerFromStore(storeId);
    }

}