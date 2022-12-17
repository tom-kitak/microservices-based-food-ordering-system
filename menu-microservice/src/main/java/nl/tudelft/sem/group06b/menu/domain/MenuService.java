package nl.tudelft.sem.group06b.menu.domain;

import java.util.ArrayList;
import java.util.List;
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
    public List<Pizza> filterPizzasByAllergens(List<Allergy> allergyList) {
        ArrayList<Pizza> ret = new ArrayList<>();
        for (Pizza p : getAllPizzas()) {
            boolean add = true;
            for (Allergy a : allergyList) {
                if (p.containsAllergen(a)) {
                    add = false;
                }
            }
            if (add) {
                ret.add(p);
            }
        }
        return ret;
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
        try {
            return this.pizzaRepository.findPizzaById(id).orElseThrow();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * gets an allergy with a specific id.
     *
     * @param id of the allergy
     * @return allergy with the given id.
     * @throws NoSuchElementException if no allergy is found.
     */
    public Allergy getAllergyById(Long id) throws NoSuchElementException {
        return this.allergyRepository.findAllergyById(id).orElseThrow();
    }

    /**
     * removes pizza with a specific id.
     *
     * @param id of pizza to remove.
     * @return true if removed/false if no pizza with that id.
     * @throws IllegalArgumentException if id is null
     */
    public boolean removePizzaById(Long id) throws IllegalArgumentException {
        try {
            if (this.pizzaRepository.findPizzaById(id).isEmpty()) {
                return false;
            }
            this.pizzaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * removes a topping with a specific id.
     *
     * @param id of the topping to remove.
     * @return true if removed/false if no topping with that id.
     * @throws IllegalArgumentException if id is null
     */
    public boolean removeToppingById(Long id) throws NoSuchElementException {
        try {
            if (this.toppingRepository.findToppingById(id).isEmpty()) {
                return false;
            }
            this.toppingRepository.deleteToppingById(id);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * adds a topping to the repository.
     *
     * @param t the topping to save.
     * @return true if topping was saved/false if there is a topping
     */
    public boolean addTopping(Topping t) throws IllegalArgumentException {
        try {
            if (this.toppingRepository.findToppingById(t.getId()).isPresent()) {
                return false;
            }
            this.toppingRepository.save(t);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * adds a pizza to the repository.
     *
     * @param p the pizza to add.
     * @return true if saved/false if there is a pizza with that id.
     * @throws IllegalArgumentException if p is null.
     */
    public boolean addPizza(Pizza p) throws IllegalArgumentException {
        try {
            if (this.pizzaRepository.findPizzaById(p.getId()).isPresent()) {
                return false;
            }
            this.pizzaRepository.save(p);
            return true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * adds an allergy to the repository.
     *
     * @param a the allergy to be added.
     * @return true if added/false if allergy exists already.
     * @throws IllegalArgumentException if allergy is null.
     */
    public boolean addAllergy(Allergy a) throws IllegalArgumentException {
        try {
            if (this.allergyRepository.findAllergyById(a.getId()).isPresent()) {
                return false;
            }
            this.allergyRepository.save(a);
            return true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }
}



