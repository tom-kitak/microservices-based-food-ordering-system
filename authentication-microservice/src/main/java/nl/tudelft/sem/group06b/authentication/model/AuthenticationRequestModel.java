package nl.tudelft.sem.group06b.authentication.model;

import lombok.Data;

/**
 * Model representing an authentication request.
 */
@Data
public class AuthenticationRequestModel {
    private String username;
    private String password;
}
