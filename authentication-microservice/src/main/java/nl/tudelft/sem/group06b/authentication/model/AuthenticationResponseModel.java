package nl.tudelft.sem.group06b.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing an authentication response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseModel {
    private String jwtToken;
}
