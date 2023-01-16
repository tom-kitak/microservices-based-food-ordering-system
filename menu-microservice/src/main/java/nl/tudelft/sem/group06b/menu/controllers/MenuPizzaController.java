package nl.tudelft.sem.group06b.menu.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.menu.domain.MenuPizzaService;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import nl.tudelft.sem.group06b.menu.models.PriceModel;
import nl.tudelft.sem.group06b.menu.models.ValidModel;
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
public class MenuPizzaController {

    private final transient MenuPizzaService menuService;


    /**
     * gets a pizza with a specific id.
     *
     * @param itemId the id of the item.
     * @return Optional of the pizza.
     */
    @GetMapping("getPizzaByID/{itemId}")
    public ResponseEntity<Optional<Pizza>> getPizzaById(@PathVariable Long itemId) {
        return ResponseEntity.ok(menuService.getPizzaById(itemId));
    }

    /**
     * returns all the pizzas in the repository.
     *
     * @return list of pizzas
     */
    @GetMapping("getAllPizzas")
    public ResponseEntity<List<Pizza>> getAllPizzas() {
        return ResponseEntity.ok(menuService.getAllPizzas());
    }

    /**
     * removes a pizza with a specific id.
     *
     * @param itemId id of the pizza.
     * @return encapsulated true if item has been successfully deleted.
     *      False if there is no item with that ID.
     */
    @DeleteMapping("remove/pizza/{itemId}")
    public ResponseEntity<Boolean> removePizza(@PathVariable Long itemId) {
        return ResponseEntity.ok(menuService.removePizzaById(itemId));
    }

    /**
     * adds a pizza to the repository.
     *
     * @param pizza to add to the repo.
     * @return encapsulated true if saved/false if another pizza with the same id.
     */
    @PostMapping("add/pizza/")
    public ResponseEntity<Boolean> addPizza(@RequestBody Pizza pizza) {
        System.out.println(pizza.toString());
        return ResponseEntity.ok(menuService.addPizza(pizza));
    }

    /**
     * gets prices for a pizza.
     *
     * @param request the ids of the pizza and toppings.
     * @return price of pizza and toppings.
     */
    @PostMapping("getPrice")
    public ResponseEntity<BigDecimal> getPrice(@RequestBody PriceModel request) {
        return ResponseEntity.ok(this.menuService.getPrice(request.getId(), request.getToppingIds()));
    }

    /**
     * checks if a pizza is valid.
     *
     * @param request the pizza and toppings.
     * @return encapsulated true if valid/false if not.
     */
    @PostMapping("isValid")
    public ResponseEntity<Boolean> isValid(@RequestBody ValidModel request) {
        return ResponseEntity.ok(this.menuService.isValidPizzaList(request.getId(), request.getToppingIds()));
    }
}
