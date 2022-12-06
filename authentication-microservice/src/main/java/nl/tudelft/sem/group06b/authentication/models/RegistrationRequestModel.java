package nl.tudelft.sem.group06b.authentication.models;

import lombok.Data;

/**
 * Model representing a registration request.
 */
@Data
public class RegistrationRequestModel {
    private String netId;
    private String password;
}