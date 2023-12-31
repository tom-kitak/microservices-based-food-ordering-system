package nl.tudelft.sem.group06b.menu.controllers;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import nl.tudelft.sem.group06b.menu.service.MenuToppingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/menu")
public class MenuToppingController {

    private final transient MenuToppingService menuToppingService;

    /**
     * fetches the topping that has a specific id.
     *
     * @return the topping with the specific id.
     */
    @GetMapping("getToppingById/{itemId}")
    public ResponseEntity<Optional<Topping>> getToppingById(@PathVariable Long itemId) {
        try {
            return ResponseEntity.ok(menuToppingService.getToppingById(itemId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

    /**
     * returns all the toppings in the repository.
     *
     * @return ResponseEntity of a list of toppings
     */
    @GetMapping("getAllToppings")
    public ResponseEntity<List<Topping>> getAllToppings() {
        return ResponseEntity.ok(menuToppingService.getAllToppings());
    }

    /**
     * removes topping with a specific id.
     *
     * @param itemId of the topping to remove.
     * @return encapsulated true if element has been succesfully deleted.
    False if there is no item with that ID.
     */
    @DeleteMapping("remove/topping/{itemId}")
    public ResponseEntity<Boolean> removeTopping(@PathVariable Long itemId) {
        try {
            return ResponseEntity.ok(menuToppingService.removeToppingById(itemId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

    /**
     * adds a topping to the repository.
     *
     * @param topping to add to the repo.
     * @return encapsulated true if saved/false if another topping with same id.
     */
    @PostMapping("add/topping/")
    public ResponseEntity<Boolean> addTopping(@RequestBody Topping topping) {
        return ResponseEntity.ok(menuToppingService.addTopping(topping));
    }

    /**
     * returns if a topping is valid.
     *
     * @param itemId of the topping.
     * @return encapsulated true/false.
     */
    @GetMapping("isValidTopping/{itemId}")
    public ResponseEntity<Boolean> isValidTopping(@PathVariable Long itemId) {
        try {
            return ResponseEntity.ok(menuToppingService.getToppingById(itemId).isPresent());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

}
