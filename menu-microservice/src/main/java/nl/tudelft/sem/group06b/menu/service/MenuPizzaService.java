package nl.tudelft.sem.group06b.menu.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.tudelft.sem.group06b.menu.authentication.AuthManager;
import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.AllergyRepository;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import nl.tudelft.sem.group06b.menu.domain.PizzaRepository;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import nl.tudelft.sem.group06b.menu.domain.ToppingRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MenuPizzaService {

    private final transient PizzaRepository pizzaRepository;

    private final transient AuthManager authManager;

    private final transient ToppingRepository toppingRepository;

    private final transient MenuAllergyService menuAllergyService;

    private final transient MenuToppingService menuToppingService;

    private final transient String regionalManager =  "regional_manager";

    /**
     * constructor for pizza service.
     *
     * @param pr pizza repo.
     * @param authManager authmanager.
     * @param ar allergy repo.
     * @param tr topping repo.
     */
    public MenuPizzaService(PizzaRepository pr, AuthManager authManager, AllergyRepository ar, ToppingRepository tr) {
        this.menuAllergyService = new MenuAllergyService(ar, pr, tr, authManager);
        this.menuToppingService = new MenuToppingService(tr, authManager, ar, pr);
        this.pizzaRepository = pr;
        this.toppingRepository = tr;
        this.authManager = authManager;
    }

    /**
     * adds a pizza to the repository.
     *
     * @param p the pizza to add.
     * @return true if saved/false if there is a pizza with that id.
     */
    public boolean addPizza(Pizza p) throws Exception {
        if (!authManager.getRole().equals(regionalManager)) {
            return false;
        }
        try {
            if (this.pizzaRepository.findPizzaById(p.getId()).isPresent()) {
                return false;
            }
            for (Topping t : p.getToppings()) {
                Optional<Topping> optTop = this.toppingRepository.findToppingById(t.getId());
                if (optTop.isEmpty()) {
                    return false;
                }
                if (!optTop.get().hasSameIds(t)) {
                    return false;
                }
            }
            this.pizzaRepository.save(p);
            this.pizzaRepository.flush();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * returns all pizzas in repository.
     *
     * @return list of all pizzas in repository.
     */
    public List<Pizza> getAllPizzas() {
        return this.pizzaRepository.findAll();
    }

    /**
     * returns the specific pizza with the given id.
     *
     * @param id of pizza.
     * @return the pizza object that has the id.
     */
    public Optional<Pizza> getPizzaById(Long id) {
        Optional<Pizza> ret = this.pizzaRepository.findPizzaById(id);
        this.pizzaRepository.flush();
        return ret;
    }

    /**
     * removes pizza with a specific id.
     *
     * @param id of pizza to remove.
     * @return true if removed/false if no pizza with that id.
     */
    public boolean removePizzaById(Long id) {
        if (!authManager.getRole().equals(regionalManager)) {
            return false;
        }
        try {
            if (this.pizzaRepository.findPizzaById(id).isEmpty()) {
                return false;
            }
            this.pizzaRepository.deleteById(id);
            this.pizzaRepository.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * checks if a list of pizzas is valid.
     *
     * @param pid the id of the pizza to check.
     * @param toppingIds the list of toppings to check.
     * @return true if they are valid/false if they aren't

     */
    public boolean isValidPizzaList(Long pid, List<Long> toppingIds) {
        try {
            if (this.pizzaRepository.findPizzaById(pid).isEmpty()) {
                return false;
            }

            for (Long id : toppingIds) {
                if (this.toppingRepository.findToppingById(id).isEmpty()) {
                    return false;
                }
            }
            this.toppingRepository.flush();
            this.pizzaRepository.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * gets price of pizza.
     *
     * @param id of pizza.
     * @param toppingIds ids of toppings.
     * @return price.
     */
    public BigDecimal getPrice(Long id, List<Long> toppingIds) throws Exception{
        Optional<Pizza> p = getPizzaById(id);
        if (p.isEmpty()) {
            return new BigDecimal("0.0");
        }
        BigDecimal ret = p.get().getPrice();
        for (Topping t : p.get().getToppings()) {
            ret = ret.add(t.getPrice());
        }
        for (Long l : toppingIds) {
            Optional<Topping> t = toppingRepository.findToppingById(l);
            if (t.isPresent()) {
                ret = ret.add(t.get().getPrice());
            }
        }
        return ret;
    }

    /**
     * checks a pizza list for allergies.
     *
     * @param pizzaId the id for the pizza.
     * @param toppingList the ids for the toppings.
     * @param allergies the allergies to check for.
     * @return formatted string if found/empty if not.
     */
    public Optional<String> checkForAllergies(Long pizzaId, List<Long> toppingList, List<String> allergies)
            throws Exception {
        String ret = "";
        List<Allergy> allergyList = menuAllergyService.getAllergiesFromStrings(allergies);
        System.out.println(allergyList);

        Optional<Pizza> currPizza = getPizzaById(pizzaId);
        System.out.println(currPizza);
        if (currPizza.isPresent()) {
            System.out.println("isPresent");
            for (Allergy a : allergyList) {
                System.out.println(a);
                System.out.println(currPizza.get().containsAllergen(a));
                if (currPizza.get().containsAllergen(a).isPresent()) {
                    ret += "Pizza name: " + currPizza.get().getName() + "; ";
                }
            }
        }

        for (Long l : toppingList) {
            Optional<String> s = menuToppingService.checkForAllergiesTopping(l, allergies);
            if (s.isPresent()) {
                ret += "Topping: " + s.get();
            }
        }

        if (ret.equals("")) {
            return Optional.empty();
        }
        return Optional.of(ret);
    }


}
