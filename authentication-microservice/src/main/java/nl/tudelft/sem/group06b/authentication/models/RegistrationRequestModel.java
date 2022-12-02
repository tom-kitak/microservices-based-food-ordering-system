package nl.tudelft.sem.group06b.authentication.models;

import lombok.Data;

import java.util.Optional;

/**
 * Model representing a registration request.
 */
@Data
public class RegistrationRequestModel {
    private String netId;
    private String password;
    private Optional<String> roleOptional;
}