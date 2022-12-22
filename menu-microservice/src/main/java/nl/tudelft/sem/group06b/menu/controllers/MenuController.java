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
import nl.tudelft.sem.group06b.menu.models.ContainsAllergenModel;
import nl.tudelft.sem.group06b.menu.models.PriceModel;
import nl.tudelft.sem.group06b.menu.models.ValidModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    @DeleteMapping("remove/pizza/{itemId}")
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
    @DeleteMapping("remove/topping/{itemId}")
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
            System.out.println(pizza.toString());
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
    @GetMapping("getStaticPizza")
    public ResponseEntity<Pizza> pizza() {
        Allergy a = new Allergy(42L, "Gluten");
        Topping t1 = new Topping(12L, "Cheese", List.of(a), new BigDecimal("3.00"));
        Pizza p = new Pizza(99L, List.of(t1), "Dough", new BigDecimal("11.00"));
        return ResponseEntity.ok(p);
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
        try {
            return ResponseEntity.ok(this.menuService.isValidPizzaList(request.getId(), request.getToppingIds()));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
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
            return ResponseEntity.ok(this.menuService.getToppingById(itemId).isPresent());
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * checks if a topping contains an allergy.
     *
     * @param itemId the id to check.
     * @return string of everything/empty if no allergy.
     */
    @PostMapping("containsAllergenTopping/{itemId}/{memberId}")
    public ResponseEntity<String> containsAllergenTopping(@PathVariable Long itemId, @PathVariable String memberId) {
        Optional<String> ret =
                this.menuService.checkForAllergiesTopping(itemId, getAllergens(memberId));
        if (ret.isEmpty()) {
            return ResponseEntity.ok("");
        }
        return ResponseEntity.ok(
                this.menuService.checkForAllergiesTopping(
                        itemId, getAllergens(this.authManager.getMemberId())).get());
    }

    /**
     * checks if a pizza contains allergens.
     *
     * @param request the pizza, toppings and the memberId.
     * @return string of allergies, empty if none.
     */
    @PostMapping("containsAllergen")
    public ResponseEntity<String> containsAllergen(@RequestBody ContainsAllergenModel request) {
        try {
            String ret;
            if (this.menuService.checkForAllergies(
                    request.getId(), request.getToppingIds(), getAllergens(request.getMemberId())).isPresent()) {
                ret = "You might be allergic!: "
                        + this.menuService.checkForAllergies(
                                request.getId(), request.getToppingIds(), getAllergens(request.getMemberId())).get();
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
    @GetMapping("filteredPizzasByAllergens")
    public ResponseEntity<List<Pizza>> filterPizzasByAllergens() {
        return ResponseEntity.ok(this.menuService.filterPizzasByAllergens(getAllergens(authManager.getMemberId())));
    }

    /**
     * returns a list of toppings without given allergens.
     *
     * @return list of toppings.
     */
    @GetMapping("filteredToppingsByAllergens")
    public ResponseEntity<List<Topping>> filterToppingsByAllergens() {
        return ResponseEntity.ok(this.menuService.filterToppingsByAllergens(getAllergens(authManager.getMemberId())));
    }

    /**
     * removes a pizza with a specific id.
     *
     * @param itemId the item to remove.
     * @return encapsulated true if successful, false if not.
     */
    @DeleteMapping("removePizzaById/{itemId}")
    public ResponseEntity<Boolean> removePizzaById(@PathVariable Long itemId) {
        try {
            return ResponseEntity.ok(this.menuService.removePizzaById(itemId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
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


