package nl.tudelft.sem.group06b.store.api;

import java.util.List;
import java.util.Locale;
import nl.tudelft.sem.group06b.store.authentication.AuthManager;
import nl.tudelft.sem.group06b.store.domain.email.Email;
import nl.tudelft.sem.group06b.store.model.SendEmailRequestModel;
import nl.tudelft.sem.group06b.store.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final transient EmailService emailService;

    private final transient AuthManager authManager;

    private transient List<Email> dummyEmails;

    private final transient String regionalManager = "regional_manager";

    /**
     * Instantiates a new controller.
     *
     * @param emailService The email service.
     */
    @Autowired
    public EmailController(AuthManager authManager, EmailService emailService) {
        this.authManager = authManager;
        this.emailService = emailService;
    }

    /**
     * Query all dummy emails.
     *
     * @return A list of dummy emails.
     */
    @GetMapping("/showAllEmails")
    public ResponseEntity<List<Email>> queryAllEmails() {
        if (authManager.getRole().toLowerCase(Locale.ROOT).equals(regionalManager)) {
            return ResponseEntity.ok(emailService.queryAllEmails());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only regional managers can view all emails");
    }

    /**
     * Query all dummy emails from a specific store.
     *
     * @param id The store id.
     * @return A list of dummy emails.
     */
    @GetMapping("/showEmailsByStoreId/{id}")
    public ResponseEntity<List<Email>> queryEmailsByStore(@PathVariable("id") Long id) {
        if (!authManager.getRole().toLowerCase(Locale.ROOT).equals(regionalManager)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only regional managers can view emails by store");
        }
        try {
            dummyEmails = emailService.getEmailsFromStore(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok(dummyEmails);
    }

    /**
     * Send dummy emails to a specific store.
     *
     * @param request serialized Email object in the form of a JSON in the request body.
     * @return an HTTP response (200 if the email is saved, 400 otherwise).
     */
    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestBody SendEmailRequestModel request) {
        if (!emailService.validateManager(authManager.getMemberId())
                && !authManager.getRole().toLowerCase(Locale.ROOT).equals(regionalManager)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only store managers and regional managers can send emails");
        }
        try {
            Long storeId = request.getStoreId();
            String message = request.getEmail();
            emailService.receiveEmail(message, storeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok("Email sent!");
    }

    /**
     * Remove a dummy email from the database.
     *
     * @param id The email id.
     * @return an HTTP response (200 if the email is removed, 400 otherwise).
     */
    @DeleteMapping("/deleteEmail/{emailId}")
    public ResponseEntity<String> deleteEmail(@PathVariable("emailId") Long id) {
        if (!authManager.getRole().toLowerCase(Locale.ROOT).equals(regionalManager)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only regional managers can delete emails");
        }
        try {
            emailService.deleteEmail(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok("Email deleted!");
    }

    /**
     * Remove a dummy email from a specific store from the database.
     *
     * @param emailId The email id.
     * @param storeId The store id.
     * @return an HTTP response (200 if the email is removed, 400 otherwise).
     * @throws Exception If store does not exist.
     */
    @DeleteMapping("/deleteEmailByStore/{emailId}/{storeId}")
    public ResponseEntity<String> deleteEmailByStore(@PathVariable("emailId") Long emailId,
                                                     @PathVariable("storeId") Long storeId)
            throws Exception {
        if (!emailService.validateManager(authManager.getMemberId())
                && !emailService.getManagerFromStore(storeId).equals(authManager.getMemberId())
                && !authManager.getRole().toLowerCase(Locale.ROOT).equals(regionalManager)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Store manager can only delete emails from his own store,"
                            + " or you need to be a regional manager");
        }
        try {
            emailService.deleteEmail(emailId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok("Email deleted!");
    }

}
