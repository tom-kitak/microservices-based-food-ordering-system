package nl.tudelft.sem.group06b.user.controllers;

import java.util.List;
import nl.tudelft.sem.group06b.user.authentication.AuthManager;
import nl.tudelft.sem.group06b.user.domain.Allergy;
import nl.tudelft.sem.group06b.user.domain.Role;
import nl.tudelft.sem.group06b.user.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    private final transient AuthManager authManager;
    private final transient UserService userService;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     * @param userService the User Service
     *
     */
    @Autowired
    public UserController(AuthManager authManager, UserService userService) {
        this.authManager = authManager;
        this.userService = userService;
    }

    /**
     * Gets allergens by memberId.
     *
     * @return the list of allergens found in the database with the given memberId
     */
    @GetMapping("/user/{memberId}/allergens")
    public ResponseEntity<List<Allergy>> getAllergens(@PathVariable String memberId) throws Exception {
        return ResponseEntity.ok(userService.getUser(memberId).getAllergies());
    }

    /**
     * Adds user to the database.
     *
     * @return 200 OK code
     */
    @PostMapping("/register/user")
    public ResponseEntity registerUser() throws Exception {
        try {
            userService.addUser(authManager.getMemberId(), Role.USER_CUSTOMER, null, null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok().build();
    }


}