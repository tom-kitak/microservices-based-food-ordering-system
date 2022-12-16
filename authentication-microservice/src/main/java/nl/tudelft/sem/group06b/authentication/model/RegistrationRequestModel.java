package nl.tudelft.sem.group06b.authentication.model;

import lombok.Data;

/**
 * Model representing an authentication response.
 */
@Data
public class RegistrationRequestModel {
    private String memberId;
    private String password;
}
