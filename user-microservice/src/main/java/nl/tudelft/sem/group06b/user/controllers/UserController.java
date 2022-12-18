package nl.tudelft.sem.group06b.user.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.user.authentication.AuthManager;
import nl.tudelft.sem.group06b.user.domain.Allergy;
import nl.tudelft.sem.group06b.user.domain.Location;
import nl.tudelft.sem.group06b.user.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
public class UserController {

    private final transient AuthManager authManager;
    private final transient UserService userService;

    /**
     * Gets allergens by memberId.
     *
     * @return the list of allergens found in the database with the given memberId
     */
    @GetMapping("/user/{memberId}/getAllergens")
    public ResponseEntity<List<Allergy>> getAllergens(@PathVariable String memberId) throws Exception {
        return ResponseEntity.ok(userService.getUser(memberId).getAllergies());
    }


    /**
     * Adds an allergen to the user's list of allergies.
     *
     * @return the list of allergens after adding the new allergen
     */
    @PutMapping("/user/{memberId}/{allergen}/addAllergen")
    public ResponseEntity<List<Allergy>> addAllergen(@PathVariable String memberId,
                                                      @PathVariable String allergen) throws Exception {
        try {
            userService.addAllergy(memberId, new Allergy(allergen));
            return ResponseEntity.ok(userService.getUser(memberId).getAllergies());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Removes allergen from the user's list of allergies.
     *
     * @return the list of allergens after removing the existing allergen
     */
    @DeleteMapping("/user/{memberId}/{allergen}/removeAllergen")
    public ResponseEntity<List<Allergy>> removeAllergen(@PathVariable String memberId,
                                                         @PathVariable String allergen) throws Exception {
        try {
            userService.removeAllergy(memberId, new Allergy(allergen));
            return ResponseEntity.ok(userService.getUser(memberId).getAllergies());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Removes allergen from the user's list of allergies.
     *
     * @return the list of allergens after removing the existing allergen
     */
    @DeleteMapping("/user/{memberId}/removeAllAllergens")
    public ResponseEntity<List<Allergy>> removeAllergens(@PathVariable String memberId) throws Exception {
        try {
            userService.removeAllAllergies(memberId);
            return ResponseEntity.ok(userService.getUser(memberId).getAllergies());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Updates the location of the user's preferred store.
     *
     * @return the location of the new preferred store
     */
    @PutMapping("/user/{memberId}/{address}/updateLocation")
    public ResponseEntity<Location> updateLocation(@PathVariable String memberId,
                                                @PathVariable String address) throws Exception {
        try {
            userService.updateLocation(memberId, new Location(address));
            return ResponseEntity.ok(userService.getUser(memberId).getPreferredLocation());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Resets the location of the user's preferred store.
     *
     * @return 200 OK response
     */
    @DeleteMapping("/user/{memberId}/resetLocation")
    public ResponseEntity resetLocation(@PathVariable String memberId)
            throws Exception {
        try {
            userService.resetLocation(memberId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    /**
     * Adds user to the database.
     *
     * @return 200 OK code
     */
    @PostMapping("/register/user")
    public ResponseEntity registerUser() throws Exception {
        try {
            userService.addUser(authManager.getMemberId(), null, null);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
