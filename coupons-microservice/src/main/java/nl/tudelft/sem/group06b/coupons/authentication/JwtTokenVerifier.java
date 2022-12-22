package nl.tudelft.sem.group06b.coupons.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Verifies the JWT token in the request for validity.
 */
@Component
public class JwtTokenVerifier {
    @Value("${jwt.secret}")  // automatically loads jwt.secret from resources/application.properties
    private transient String jwtSecret;

    /**
     * Validate the JWT token for expiration.
     */
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public String getMemberIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    /**
     * Get the roles from the JWT token.
     *
     * @param token The JWT token
     * @return The roles
     */
    public GrantedAuthority getAuthoritiesFromToken(String token) {
        // Calls getClaimFromToken, as arguments passes token and a function that takes a Claims object and returns
        // the authorities.
        return getClaimFromToken(token, (claims) -> new SimpleGrantedAuthority(
                ((List<Map<String, String>>) claims.get("role")).get(0).get("authority")
        ));
    }
}
