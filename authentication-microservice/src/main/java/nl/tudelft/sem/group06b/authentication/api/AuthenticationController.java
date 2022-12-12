package nl.tudelft.sem.group06b.authentication.api;

import lombok.AllArgsConstructor;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final transient AuthenticationService authenticationService;

    /**
     * This endpoint will authenticate the user given their credentials and if valid it will generate a JWT token that
     * contains their authorities.
     *
     * @param request the credentials used for authentication
     * @return a jwtToken represented as a String
     */
    @PostMapping("/authenticate")
    private ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request) {
        try {
            String jwtToken = authenticationService.authenticate(new MemberId(request.getMemberId()),
                                                          new Password(request.getPassword()));
            return ResponseEntity.ok(new AuthenticationResponseModel(jwtToken));
        } catch (DisabledException disabledException) {
            System.out.println("Disabled exception");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_DISABLED", disabledException);
        } catch (BadCredentialsException badCredentialsException) {
            System.out.println("Bad credentials exception");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", badCredentialsException);
        }

    }

    /**
     * This method will create a new account for a user if possible and return ok if succeeds.
     *
     * @param request the credentials of that account
     * @return an empty response with message which indicate success
     */
    @PostMapping("/register")
    private ResponseEntity<?> register(@RequestBody RegistrationRequestModel request) {
        try {
            authenticationService.register(new MemberId(request.getMemberId()), new Password(request.getPassword()));
        } catch (MemberIdAlreadyInUseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Creating a new role for the user if the person sending the request is authorized to perform this action.
     *
     * @param request the new name of the role
     * @return an empty response with message which indicate success
     */
    @PostMapping("/create_role")
    private ResponseEntity<?> createRole(@RequestBody RoleCreationRequestModel request) {
        //TODO: add authorization for this endpoint so that only the regional manager can query this.
        try {
            authenticationService.createRole(new RoleName(request.getRoleName()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * This method changes the role of a user if the person requesting it is authorized to perform this type of action.
     *
     * @param request the user identifier (memberId) and the new role of that user
     * @return an empty response with message which indicate success
     */
    @PutMapping("/change_role")
    private ResponseEntity<?> changeRole(@RequestBody ChangeRoleRequestModel request) {
        //TODO: add authorization for this endpoint so that only the regional manager can query this.
        try {
            authenticationService.changeRole(new MemberId(request.getMemberId()), new RoleName(request.getNewRoleName()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}

