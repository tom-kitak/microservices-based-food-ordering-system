package nl.tudelft.sem.group06b.menu.controllers;

import java.util.List;
import nl.tudelft.sem.group06b.menu.authentication.AuthManager;
import nl.tudelft.sem.group06b.menu.domain.MenuService;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class MenuController {

    private final transient AuthManager authManager;
    private final transient MenuService menuService;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     * @param menuService menu service
     */
    @Autowired
    public MenuController(AuthManager authManager, nl.tudelft.sem.group06b.menu.domain.MenuService menuService) {
        this.authManager = authManager;
        this.menuService = menuService;
    }

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
    @PostMapping("getToppingByID/{itemID}")
    public ResponseEntity<Pizza> getItem() {
        //TODO
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * returns all the pizzas in the repository.
     *
     * @return list of pizzas
     */
    @GetMapping("menu/pizzas")
    public ResponseEntity<List<Pizza>> getAll() {
        //TODO
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

}
