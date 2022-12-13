package nl.tudelft.sem.group06b.authentication.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenGenerator {

    /**
     * Generate a JWT token for the provided user.
     *
     * @param userDetails The user details
     * @return the JWT token
     */
    String generateToken(UserDetails userDetails);
}
