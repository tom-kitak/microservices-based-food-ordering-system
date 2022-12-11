package nl.tudelft.sem.group06b.authentication.api;

import nl.tudelft.sem.group06b.authentication.domain.user.MemberID;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberIDAlreadyInUseException;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.model.AuthenticationRequestModel;
import nl.tudelft.sem.group06b.authentication.model.AuthenticationResponseModel;
import nl.tudelft.sem.group06b.authentication.model.RegistrationRequestModel;
import nl.tudelft.sem.group06b.authentication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.PasswordAuthentication;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final transient AuthenticationService authenticationService;

    /**
     * Constructor for the authentication controller.
     *
     * @param authenticationService the service for the authentication
     */
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    private ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request) {
        String jwtToken;
        System.out.println("Got an authentication request");
        try {
            jwtToken = authenticationService.authenticate(new MemberID(request.getMemberID()), new Password(request.getPassword()));
        } catch (DisabledException disabledException) {
            System.out.println("Disabled exception");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_DISABLED", disabledException);
        } catch (BadCredentialsException badCredentialsException) {
            System.out.println("Bad credentials exception");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", badCredentialsException);
        }
        return ResponseEntity.ok(new AuthenticationResponseModel(jwtToken));
    }

    @PostMapping("/register")
    private ResponseEntity register(@RequestBody RegistrationRequestModel request) {
        try {
            authenticationService.register(new MemberID(request.getMemberID()), new Password(request.getPassword()));
        } catch (MemberIDAlreadyInUseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
