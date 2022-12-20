package nl.tudelft.sem.group06b.store.api;

import java.util.List;
import nl.tudelft.sem.group06b.store.database.EmailRepository;
import nl.tudelft.sem.group06b.store.domain.Email;
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

    private final transient EmailRepository emailRepository;

    private transient List<Email> dummyEmails;

    /**
     * Instantiates a new controller.
     *
     * @param emailService The email service.
     * @param emailRepository The email repo.
     */
    @Autowired
    public EmailController(EmailService emailService, EmailRepository emailRepository) {
        this.emailService = emailService;
        this.emailRepository = emailRepository;
    }

    /**
     * Query all dummy emails.
     *
     * @return A list of dummy emails.
     */
    @GetMapping("/showAllEmails")
    public ResponseEntity<List<Email>> queryAllEmails() {
        return ResponseEntity.ok(emailRepository.findAll());
    }

    /**
     * Query all dummy emails from a specific store.
     *
     * @param id The store id.
     * @return A list of dummy emails.
     */
    @GetMapping("/showEmailsByStoreId/{id}")
    public ResponseEntity<List<Email>> queryEmailsByStore(@PathVariable("id") Long id) {

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
    public ResponseEntity<?> sendEmail(@RequestBody SendEmailRequestModel request) {

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
    public ResponseEntity<?> deleteEmail(@PathVariable("emailId") Long id) {

        try {
            emailService.deleteEmail(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok("Email deleted!");
    }



}
