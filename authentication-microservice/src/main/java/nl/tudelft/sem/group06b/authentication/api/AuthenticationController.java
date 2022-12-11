package nl.tudelft.sem.group06b.authentication.api;

import nl.tudelft.sem.group06b.authentication.domain.role.RoleName;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberId;
import nl.tudelft.sem.group06b.authentication.domain.user.MemberIdAlreadyInUseException;
import nl.tudelft.sem.group06b.authentication.domain.user.Password;
import nl.tudelft.sem.group06b.authentication.model.AuthenticationRequestModel;
import nl.tudelft.sem.group06b.authentication.model.AuthenticationResponseModel;
import nl.tudelft.sem.group06b.authentication.model.ChangeRoleRequestModel;
import nl.tudelft.sem.group06b.authentication.model.RegistrationRequestModel;
import nl.tudelft.sem.group06b.authentication.model.RoleCreationRequestModel;
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

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final transient AuthenticationService authenticationService;

    /**
     * Constructor for the authentication controller.
     *
     * @param authenticationService the service that contains the authentication business logic
     */
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    private ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request) {
        String jwtToken;
        try {
            jwtToken = authenticationService.authenticate(new MemberId(request.getMemberId()),
                                                          new Password(request.getPassword()));
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
            authenticationService.register(new MemberId(request.getMemberId()), new Password(request.getPassword()));
        } catch (MemberIdAlreadyInUseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create_role")
    private ResponseEntity createRole(@RequestBody RoleCreationRequestModel request) {
        //TODO: add authorization for this endpoint so that only the regional manager can query this.
        try {
            authenticationService.createRole(new RoleName(request.getRoleName()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change_role")
    private ResponseEntity changeRole(@RequestBody ChangeRoleRequestModel request) {
        //TODO: add authorization for this endpoint so that only the regional manager can query this.
        try {
            authenticationService.changeRole(new MemberId(request.getMemberId()), new RoleName(request.getNewRoleName()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}

