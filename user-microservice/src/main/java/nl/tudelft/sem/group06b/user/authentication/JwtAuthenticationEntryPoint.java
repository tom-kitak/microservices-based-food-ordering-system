<<<<<<<< HEAD:example-microservice/src/main/java/nl/tudelft/sem/group06b/example/authentication/JwtAuthenticationEntryPoint.java
package nl.tudelft.sem.group06b.example.authentication;
========
package nl.tudelft.sem.group06b.user.authentication;
>>>>>>>> dev:user-microservice/src/main/java/nl/tudelft/sem/group06b/user/authentication/JwtAuthenticationEntryPoint.java

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.sem.group06b.example.authentication.JwtRequestFilter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Authentication entry point for JWT security.
 * <p>
 * The authentication entry point is called when an unauthenticated client tries to access a protected resource.
 * This JWT authentication entry point returns a response indicating the request was unauthorized.
 * </p>
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        // Return an unauthorized response code
        response.addHeader(JwtRequestFilter.WWW_AUTHENTICATE_HEADER, JwtRequestFilter.AUTHORIZATION_AUTH_SCHEME);
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }
}