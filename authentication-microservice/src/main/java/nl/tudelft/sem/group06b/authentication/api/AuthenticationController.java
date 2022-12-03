package nl.tudelft.sem.group06b.authentication.api;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.authentication.model.AuthenticationRequestModel;
import nl.tudelft.sem.group06b.authentication.model.AuthenticationResponseModel;
import nl.tudelft.sem.group06b.authentication.model.RegistrationRequestModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

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
