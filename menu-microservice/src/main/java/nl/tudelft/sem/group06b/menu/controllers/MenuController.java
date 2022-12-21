package nl.tudelft.sem.group06b.menu.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.menu.authentication.AuthManager;
import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.MenuService;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/menu")
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
    public ResponseEntity<Optional<Topping>> getToppingById(@PathVariable Long itemId) throws ResponseStatusException {
        try {
            return ResponseEntity.ok(menuService.getToppingById(itemId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * gets a pizza with a specific id.
     *
     * @param itemId the id of the item.
     * @return Optional of the pizza.
     * @throws ResponseStatusException if itemId is null or there is an exception.
     */
    @PostMapping("getPizzaByID/{itemId}")
    public ResponseEntity<Optional<Pizza>> getPizzaById(@PathVariable Long itemId) throws ResponseStatusException {
        try {
            return ResponseEntity.ok(menuService.getPizzaById(itemId));
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
    public ResponseEntity<List<Pizza>> getAllPizzas() {
        return ResponseEntity.ok(menuService.getAllPizzas());
    }

    /**
     * returns all the toppings in the repository.
     *
     * @return ResponseEntity of a list of toppings
     */
    @GetMapping("getAllToppings")
    public ResponseEntity<List<Topping>> getAllToppings() {
        return ResponseEntity.ok(menuService.getAllToppings());
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
            System.out.println(e.getMessage());
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

    /**
     * adds an allergy to the repository.
     *
     * @param allergy to add.
     * @return encapsulated true if added/false if couldn't.
     * @throws ResponseStatusException if something went wrong.
     */
    @PostMapping("add/Allergy")
    public ResponseEntity<Boolean> addAllergy(@RequestBody Allergy allergy) throws ResponseStatusException {
        try {
            return ResponseEntity.ok(menuService.addAllergy(allergy));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * gets a static topping for testing.
     *
     * @return static topping.
     */
    @GetMapping("getStaticTopping")
    public ResponseEntity<Topping> topping() {
        Allergy a = new Allergy(42L, "Gluten");
        return ResponseEntity.ok(new Topping(42L, "Pepperoni", List.of(a), new BigDecimal("24.99")));
    }

    /**
     * gets Price of pizza.
     *
     * @param id of pizza.
     * @param toppings of pizza.
     * @return price of pizza.
     */
    @PostMapping("getPrice")
    public ResponseEntity<BigDecimal> getPrice(@RequestBody Long id, @RequestBody List<Long> toppings) {
        return ResponseEntity.ok(this.menuService.getPrice(id, toppings));
    }

    /**
     * checks if a list of pizzas is valid.
     *
     * @param ids the ids of the pizzas.
     * @param toppingIds the ids of the toppings.
     * @return encapsulated true if valid/false if not.
     */
    @PostMapping("isValid")
    public ResponseEntity<Boolean> isValid(@RequestBody Long ids, @RequestBody List<Long> toppingIds) {
        try {
            return ResponseEntity.ok(this.menuService.isValidPizzaList(ids, toppingIds));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * checks if a pizzas contain allergens.
     *
     * @param id the id of the pizzas.
     * @param toppingIds the ids of the toppings.
     * @param memberId the id of the member with allergies.
     * @return optional string with the conflicts.
     */
    @PostMapping("containsAllergen")
    public ResponseEntity<String> containsAllergen(@RequestBody Long id,
                                                             @RequestBody List<Long> toppingIds,
                                                             @RequestBody String memberId) {
        try {
            String ret;
            if (this.menuService.checkForAllergies(id, toppingIds, getAllergens(memberId)).isPresent()) {
                ret = "You might be allergic!: "
                        + this.menuService.checkForAllergies(id, toppingIds, getAllergens(memberId)).get();
            } else {
                ret = "";
            }
            return ResponseEntity.ok(ret);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * returns a list of pizzas without given allergens.
     *
     * @return list of pizzas.
     */
    @PostMapping("filteredPizzasByAllergens")
    public ResponseEntity<List<Pizza>> filterPizzasByAllergens() {
        return ResponseEntity.ok(this.menuService.filterPizzasByAllergens(getAllergens(authManager.getMemberId())));
    }

    /**
     * returns a list of toppings without given allergens.
     *
     * @return list of toppings.
     */
    @PostMapping("filteredToppingsByAllergens")
    public ResponseEntity<List<Topping>> filterToppingsByAllergens() {
        return ResponseEntity.ok(this.menuService.filterToppingsByAllergens(getAllergens(authManager.getMemberId())));
    }

    /**
     * returns a list of allergens for a specific user.
     *
     * @param l the user id.
     * @return list of allergies in a string format.
     */
    public List<String> getAllergens(String l) {
        String url = "http://localhost:8082/user/" + l + "/getAllergens";

        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", String.format("Bearer %s", authManager.getToken()));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        return (List<String>) response.getBody().stream()
                .map(x -> x.toString().split("=")[1].split("}")[0]).collect(Collectors.toList());

    }


}


