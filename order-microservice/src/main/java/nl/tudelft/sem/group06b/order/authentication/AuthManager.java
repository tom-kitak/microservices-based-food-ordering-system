package nl.tudelft.sem.group06b.order.authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Authentication Manager.
 */
@Component
public class AuthManager {
    /**
     * Interfaces with spring security to get the name of the user in the current context.
     *
     * @return The name of the user.
     */
    public String getMemberId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Interfaces with spring security to get the role of the user in the current context.
     *
     * @return The role of the user.
     */
    public String getRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .toArray()[0].toString().split("=")[1].split("}")[0];
    }
}

