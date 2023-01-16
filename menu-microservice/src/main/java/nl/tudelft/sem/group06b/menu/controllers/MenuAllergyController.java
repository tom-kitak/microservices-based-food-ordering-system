package nl.tudelft.sem.group06b.menu.controllers;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.group06b.menu.authentication.AuthManager;
import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.MenuService;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import nl.tudelft.sem.group06b.menu.models.ContainsAllergenModel;
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
    private final transient MenuService menuService;

    /**
     * adds an allergy to the repository.
     *
     * @param allergy to add.
     * @return encapsulated true if added/false if couldn't.
     */
    @PostMapping("add/allergy")
    public ResponseEntity<Boolean> addAllergy(@RequestBody Allergy allergy) {
        return ResponseEntity.ok(menuService.addAllergy(allergy));
    }

    /**
     * checks if a pizza contains allergens.
     *
     * @param request the pizza, toppings and the memberId.
     * @return string of allergies, empty if none.
     */
    @PostMapping("containsAllergen")
    public ResponseEntity<String> containsAllergen(@RequestBody ContainsAllergenModel request) {
        String ret;
        if (this.menuService.checkForAllergies(
                request.getId(), request.getToppingIds(), menuService.getAllergens(request.getMemberId())).isPresent()) {
            ret = "You might be allergic!: "
                    + this.menuService.checkForAllergies(
                    request.getId(), request.getToppingIds(), menuService.getAllergens(request.getMemberId())).get();
        } else {
            ret = "";
        }
        return ResponseEntity.ok(ret);
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
                this.menuService.checkForAllergiesTopping(itemId, menuService.getAllergens(memberId));
        if (ret.isEmpty()) {
            return ResponseEntity.ok("");
        }
        return ResponseEntity.ok(
                this.menuService.checkForAllergiesTopping(
                        itemId, menuService.getAllergens(this.authManager.getMemberId())).get());
    }

    @DeleteMapping("remove/allergy/{itemId}")
    public ResponseEntity<Boolean> removeAllergyById(@PathVariable Long itemId) {
        return ResponseEntity.ok(this.menuService.removeAllergyById(itemId));
    }

    /**
     * returns a list of toppings without given allergens.
     *
     * @return list of toppings.
     */
    @GetMapping("filteredToppingsByAllergens")
    public ResponseEntity<List<Topping>> filterToppingsByAllergens() {
        return ResponseEntity.ok(this.menuService.filterToppingsByAllergens(
                menuService.getAllergens(authManager.getMemberId())));
    }

    /**
     * returns a list of pizzas without given allergens.
     *
     * @return list of pizzas.
     */
    @GetMapping("filteredPizzasByAllergens")
    public ResponseEntity<List<Pizza>> filterPizzasByAllergens() {
        return ResponseEntity.ok(this.menuService.filterPizzasByAllergens(
                menuService.getAllergens(authManager.getMemberId())));
    }

}
