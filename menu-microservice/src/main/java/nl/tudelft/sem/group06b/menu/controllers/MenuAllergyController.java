package nl.tudelft.sem.group06b.menu.controllers;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.menu.authentication.AuthManager;
import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import nl.tudelft.sem.group06b.menu.models.ContainsAllergenModel;
import nl.tudelft.sem.group06b.menu.service.MenuAllergyService;
import nl.tudelft.sem.group06b.menu.service.MenuPizzaService;
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
public class MenuAllergyController {

    private final transient AuthManager authManager;
    private final transient MenuAllergyService menuAllergyService;
    private final transient MenuToppingService menuToppingService;
    private final transient MenuPizzaService menuPizzaService;

    /**
     * adds an allergy to the repository.
     *
     * @param allergy to add.
     * @return encapsulated true if added/false if couldn't.
     */
    @PostMapping("add/allergy")
    public ResponseEntity<Boolean> addAllergy(@RequestBody Allergy allergy) {
        return ResponseEntity.ok(menuAllergyService.addAllergy(allergy));
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
            if (this.menuPizzaService.checkForAllergies(
                    request.getId(), request.getToppingIds(),
                    menuAllergyService.getAllergens(request.getMemberId())).isPresent()) {
                ret = "You might be allergic!: " + this.menuPizzaService.checkForAllergies(
                        request.getId(), request.getToppingIds(),
                        menuAllergyService.getAllergens(request.getMemberId())).get();
            } else {
                ret = "";
            }
            return ResponseEntity.ok(ret);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
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
        try {
            Optional<String> ret =
                    this.menuToppingService.checkForAllergiesTopping(itemId, menuAllergyService.getAllergens(memberId));
            if (ret.isEmpty()) {
                return ResponseEntity.ok("");
            }
            return ResponseEntity.ok(
                    this.menuToppingService.checkForAllergiesTopping(
                            itemId, menuAllergyService.getAllergens(this.authManager.getMemberId())).get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }

    }

    @DeleteMapping("remove/allergy/{itemId}")
    public ResponseEntity<Boolean> removeAllergyById(@PathVariable Long itemId) {
        return ResponseEntity.ok(menuAllergyService.removeAllergyById(itemId));
    }

    /**
     * returns a list of toppings without given allergens.
     *
     * @return list of toppings.
     */
    @GetMapping("filteredToppingsByAllergens")
    public ResponseEntity<List<Topping>> filterToppingsByAllergens() {
        return ResponseEntity.ok(menuAllergyService.filterToppingsByAllergens(
                menuAllergyService.getAllergens(authManager.getMemberId())));
    }

    /**
     * returns a list of pizzas without given allergens.
     *
     * @return list of pizzas.
     */
    @GetMapping("filteredPizzasByAllergens")
    public ResponseEntity<List<Pizza>> filterPizzasByAllergens() {
        return ResponseEntity.ok(this.menuAllergyService.filterPizzasByAllergens(
                menuAllergyService.getAllergens(authManager.getMemberId())));
    }

}
