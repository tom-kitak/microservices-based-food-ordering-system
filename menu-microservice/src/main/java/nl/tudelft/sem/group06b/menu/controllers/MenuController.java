package nl.tudelft.sem.group06b.menu.controllers;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.menu.authentication.AuthManager;
import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.MenuService;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
@AllArgsConstructor
public class MenuController {

    private final transient AuthManager authManager;
    private final transient MenuService menuService;

    /**
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello");
    }

    /**
     * fetches the topping that has a specific id.
     *
     * @return the topping with the specific id.
     */
    @PostMapping("getToppingByID/{itemId}")
    public ResponseEntity<Topping> getItem(@PathVariable Long itemId) throws ResponseStatusException {
        try {
            return ResponseEntity.ok(menuService.getToppingById(itemId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * returns all the pizzas in the repository.
     *
     * @return list of pizzas
     */
    @GetMapping("getAllPizzas")
    public ResponseEntity<List<Pizza>> getAll() {
        return ResponseEntity.ok(menuService.getAllPizzas());
    }

    /**
     * removes a pizza with a specific id.
     *
     * @param itemId id of the pizza.
     * @return encapsulated true if item has been successfully deleted.
     *      False if there is no item with that ID.
     * @throws ResponseStatusException bad request if itemId is null
     */
    @PostMapping("remove/pizza/{itemId}")
    public ResponseEntity<Boolean> removePizza(@PathVariable Long itemId) throws IllegalArgumentException {
        try {
            return ResponseEntity.ok(menuService.removePizzaById(itemId));
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * removes topping with a specific id.
     *
     * @param itemId of the topping to remove.
     * @return encapsulated true if element has been succesfully deleted.
        False if there is no item with that ID.
     * @throws ResponseStatusException bad request if itemId is null
     */
    @PostMapping("remove/topping/{itemId}")
    public ResponseEntity<Boolean> removeTopping(@PathVariable Long itemId) throws ResponseStatusException {
        try {
            return ResponseEntity.ok(menuService.removeToppingById(itemId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * adds a topping to the repository.
     *
     * @param topping to add to the repo.
     * @return encapsulated true if saved/false if another topping with same id.
     * @throws ResponseStatusException if topping is null
     */
    @PostMapping("add/topping/")
    public ResponseEntity<Boolean> addTopping(@RequestBody Topping topping) throws ResponseStatusException {
        try {
            return ResponseEntity.ok(menuService.addTopping(topping));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * adds a pizza to the repository.
     *
     * @param pizza to add to the repo.
     * @return encapsulated true if saved/false if another pizza with the same id.
     * @throws ResponseStatusException if pizza is null.
     */
    @PostMapping("add/pizza/")
    public ResponseEntity<Boolean> addPizza(@RequestBody Pizza pizza) throws ResponseStatusException {
        try {
            return ResponseEntity.ok(menuService.addPizza(pizza));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getTopping")
    public ResponseEntity<Topping> topping() {
        Allergy a = new Allergy(42L, "Gluten");
        return ResponseEntity.ok(new Topping("Pepperoni", List.of(a), new BigDecimal("24.99")));
    }
}


