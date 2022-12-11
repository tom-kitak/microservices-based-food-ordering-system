package nl.tudelft.sem.group06b.authentication.domain.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.sem.group06b.authentication.domain.providers.CurrentTimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Generator for JWT tokens.
 */
@Component
public class JwtTokenGenerator {
    /**
     * Time in milliseconds the JWT token is valid for.
     */
    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

    @Value("${jwt.secret}")  // automatically loads jwt.secret from resources/application.properties
    private transient String jwtSecret;

    /**
     * Time provider to make testing easier.
     */
    private final transient CurrentTimeProvider currentTimeProvider;

    @Autowired
    public JwtTokenGenerator(CurrentTimeProvider currentTimeProvider) {
        this.currentTimeProvider = currentTimeProvider;
    }

    /**
     * Generate a JWT token for the provided user.
     *
     * @param userDetails The user details
     * @return the JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(currentTimeProvider.getCurrentTime().toEpochMilli()))
                .setExpiration(new Date(currentTimeProvider.getCurrentTime().toEpochMilli() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }
}
