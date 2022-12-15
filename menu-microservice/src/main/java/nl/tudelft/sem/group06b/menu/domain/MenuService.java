package nl.tudelft.sem.group06b.menu.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

/**
 * MenuService gets toppings/pizzas from repositories.
 */
@Service
public class MenuService {
    /**
     * repository for pizzas.
     */
    private final transient PizzaRepository pizzaRepository;
    /**
     * repository for toppings.
     */
    private final transient ToppingRepository toppingRepository;

    private final transient AllergyRepository allergyRepository;

    /**
     * constructor for menuservice.
     *
     * @param ar repository for allergies
     * @param pr repository for pizzas.
     * @param tr repository for toppings.
     */
    public MenuService(PizzaRepository pr, ToppingRepository tr, AllergyRepository ar) {
        this.pizzaRepository = pr;
        this.toppingRepository = tr;
        this.allergyRepository = ar;
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
     * returns all toppings in directory.
     *
     * @return list of all toppings in repository.
     */
    public List<Topping> getAllToppings() {
        return this.toppingRepository.findAll();
    }

    /**
     * filters the pizzas by all the allergens of a user.
     *
     * @param allergyList list of allergies to filter out.
     * @return list of pizzas without the allergens in the list.
     */
    public List<Pizza> filterPizzasByAllergens(List<Allergy> allergyList){
        //TODO
        return new ArrayList<Pizza>();
    }

    /**
     * returns specific topping with the given id.
     *
     * @param id of the topping.
     * @return topping object that has the id.
     * @throws NoSuchElementException if no topping is found with that id.
     */
    public Topping getToppingById(Long id) throws NoSuchElementException {
        return this.toppingRepository.findToppingById(id).orElseThrow();
    }

    /**
     * returns the specific pizza with the given id.
     *
     * @param id of pizza.
     * @return the pizza object that has the id.
     * @throws NoSuchElementException if no pizza is found.
     */
    public Pizza getPizzaById(Long id) throws NoSuchElementException {
        return this.pizzaRepository.findPizzaById(id).orElseThrow();
    }

    public Allergy getAllergyById(Long id) throws NoSuchElementException {
        return this.allergyRepository.findAllergyById(id).orElseThrow();
    }
}



