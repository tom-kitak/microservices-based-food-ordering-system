package nl.tudelft.sem.group06b.authentication.api;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.authentication.domain.user.service.JwtUserDetailsService;
import nl.tudelft.sem.group06b.authentication.domain.user.service.RegistrationService;
import nl.tudelft.sem.group06b.authentication.model.AuthenticationRequestModel;
import nl.tudelft.sem.group06b.authentication.model.AuthenticationResponseModel;
import nl.tudelft.sem.group06b.authentication.model.RegistrationRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final transient AuthenticationManager authenticationManager;

    private final transient JwtUserDetailsService jwtUserDetailsService;

    private final transient RegistrationService registrationService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtUserDetailsService jwtUserDetailsService,
                                    RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.registrationService = registrationService;
    }

    @PostMapping("/authenticate")
    private ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request) {
        //TODO: implement this method
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/register")
    private ResponseEntity register(@RequestBody RegistrationRequestModel request) {
        //TODO: implement this method
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
