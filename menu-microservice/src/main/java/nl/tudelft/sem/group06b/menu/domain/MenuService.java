package nl.tudelft.sem.group06b.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        List<Topping> ret = this.toppingRepository.findAll();
        this.toppingRepository.flush();
        return ret;
    }

    /**
     * filters the pizzas by all the allergens of a user.
     *
     * @param strings list of allergies to filter out.
     * @return list of pizzas without the allergens in the list.
     */
    public List<Pizza> filterPizzasByAllergens(List<String> strings) {
        ArrayList<Allergy> allergyList = new ArrayList<>();
        for (String s : strings) {
            if (getAllergyByName(s).isPresent()) {
                allergyList.add(getAllergyByName(s).get());
            }
        }

        ArrayList<Pizza> ret = new ArrayList<>();
        for (Pizza p : getAllPizzas()) {
            boolean add = true;
            for (Allergy a : allergyList) {
                if (p.containsAllergen(a).isPresent()) {
                    add = false;
                }
            }
            if (add) {
                ret.add(p);
            }
        }
        this.allergyRepository.flush();
        return ret;
    }

    /**
     * returns specific topping with the given id.
     *
     * @param id of the topping.
     * @return topping object that has the id.
     * @throws NoSuchElementException if no topping is found with that id.
     */
    public Optional<Topping> getToppingById(Long id) {
        Optional<Topping> ret = this.toppingRepository.findToppingById(id);
        this.toppingRepository.flush();
        return ret;
    }

    /**
     * returns the specific pizza with the given id.
     *
     * @param id of pizza.
     * @return the pizza object that has the id.
     * @throws NoSuchElementException if no pizza is found.
     */
    public Optional<Pizza> getPizzaById(Long id) {
        Optional<Pizza> ret = this.pizzaRepository.findPizzaById(id);
        this.pizzaRepository.flush();
        return ret;
    }

    /**
     * gets an allergy with a specific id.
     *
     * @param id of the allergy
     * @return allergy with the given id.
     * @throws NoSuchElementException if no allergy is found.
     */
    public Optional<Allergy> getAllergyById(Long id) {
        try {
            Optional<Allergy> ret = this.allergyRepository.findAllergyById(id);
            this.allergyRepository.flush();
            return ret;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * returns allergy with a given name.
     *
     * @param name of the allergy to find.
     * @return optional of allergy/empty if not there.
     */
    public Optional<Allergy> getAllergyByName(String name) {
        try {
            Optional<Allergy> ret = this.allergyRepository.findAllergyByNameIsIgnoreCase(name);
            this.allergyRepository.flush();
            return ret;
        } catch (Exception e) {
            return Optional.empty();
        }
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
            this.pizzaRepository.flush();
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    /**
     * removes a topping with a specific id.
     *
     * @param id of the topping to remove.
     * @return true if removed/false if no topping with that id.
     * @throws IllegalArgumentException if id is null
     */
    public boolean removeToppingById(Long id) {
        if (this.toppingRepository.findToppingById(id).isEmpty()) {
            return false;
        }
        this.toppingRepository.deleteToppingById(id);
        this.toppingRepository.flush();
        return true;

    }

    /**
     * adds a topping to the repository.
     *
     * @param t the topping to add.
     * @return true if added/false if couldn't
     * @throws IllegalArgumentException if t is null.
     */
    public boolean addTopping(Topping t) throws IllegalArgumentException {
        try {
            if (this.toppingRepository.findToppingById(t.getId()).isPresent()) {
                return false;
            }
            this.toppingRepository.save(t);
            this.toppingRepository.flush();
            return true;
        } catch (Exception e) {
            System.out.println("Message: " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
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
            this.pizzaRepository.flush();
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
            if (getAllergyById(a.getId()).isPresent()) {
                return false;
            }
            this.allergyRepository.save(a);
            this.allergyRepository.flush();
            return true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * checks if a list of pizzas is valid.
     *
     * @param pid the id of the pizza to check.
     * @param toppingIds the list of toppings to check.
     * @return true if they are valid/false if they aren't
     * @throws IllegalArgumentException if one of the pizzas in the list is null.
     */
    public boolean isValidPizzaList(Long pid, List<Long> toppingIds) throws IllegalArgumentException {
        try {
            System.out.println(pid);
            System.out.println(toppingIds);
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
     * checks a pizza list for allergies.
     *
     * @param pizzaId the id for the pizza.
     * @param toppingList the ids for the toppings.
     * @param allergies the allergies to check for.
     * @return formatted string if found/empty if not.
     */
    public Optional<String> checkForAllergies(Long pizzaId, List<Long> toppingList, List<String> allergies) {
        String ret = "";
        List<Allergy> allergyList = getAllergiesFromStrings(allergies);
        System.out.println(allergyList);

        Optional<Pizza> currPizza = getPizzaById(pizzaId);
        System.out.println(currPizza);
        if (currPizza.isPresent()) {
            System.out.println("isPresent");
            for (Allergy a : allergyList) {
                System.out.println(a);
                System.out.println(currPizza.get().containsAllergen(a));
                if (currPizza.get().containsAllergen(a).isPresent()) {
                    ret += currPizza.get().containsAllergen(a).get() + ";";
                }
            }
        }

        for (Long l : toppingList) {
            Optional<String> s = checkForAllergiesTopping(l, allergies);
            if (s.isPresent()) {
                ret += s.get();
            }
        }

        if (ret.equals("")) {
            return Optional.empty();
        }
        return Optional.of(ret);
    }

    /**
     * gets allergies from a list of strings.
     *
     * @param allergies strings to check.
     * @return list of allergies.
     */
    public List<Allergy> getAllergiesFromStrings(List<String> allergies) {
        ArrayList<Allergy> allergyList = new ArrayList<>();
        //adds allergies to list
        for (String s : allergies) {
            if (getAllergyByName(s).isPresent()) {
                allergyList.add(getAllergyByName(s).get());
            }
        }
        return allergyList;
    }

    /**
     * checks if an allergy has a topping.
     *
     * @param id the id of the topping.
     * @param allergies the allergies in strings.
     * @return string of allergies/empty if none.
     */
    public Optional<String> checkForAllergiesTopping(Long id, List<String> allergies) {
        String ret = "";
        List<Allergy> allergyList = getAllergiesFromStrings(allergies);
        Optional<Topping> curr = getToppingById(id);
        if (curr.isPresent()) {
            for (Allergy a : allergyList) {
                if (curr.get().containsAllergy(a).isPresent()) {
                    ret += curr.get().containsAllergy(a).get() + ";";
                }
            }
        }
        if (ret.equals("")) {
            return Optional.empty();
        }
        return Optional.of(ret);
    }

    /**
     * gets price of pizza.
     *
     * @param id of pizza.
     * @param toppingIds ids of toppings.
     * @return price.
     */
    public BigDecimal getPrice(Long id, List<Long> toppingIds) {
        Optional<Pizza> p = getPizzaById(id);
        if (p.isEmpty()) {
            return new BigDecimal("0.0");
        }
        BigDecimal ret = p.get().getPrice();
        for (Topping t : p.get().getToppings()) {
            ret = ret.add(t.getPrice());
        }
        for (Long l : toppingIds) {
            Optional<Topping> t = getToppingById(l);
            if (t.isPresent()) {
                ret = ret.add(t.get().getPrice());
            }
        }
        return ret;
    }

    /**
     * filters all toppings by allergens.
     *
     * @param strings allergies to filter out.
     * @return the toppings.
     */
    public List<Topping> filterToppingsByAllergens(List<String> strings) {
        ArrayList<Allergy> allergyList = new ArrayList<>();
        for (String s : strings) {
            if (getAllergyByName(s).isPresent()) {
                allergyList.add(getAllergyByName(s).get());
            }
        }

        ArrayList<Topping> ret = new ArrayList<>();
        for (Topping t : getAllToppings()) {
            boolean add = true;
            for (Allergy a : allergyList) {
                if (t.containsAllergy(a).isPresent()) {
                    add = false;
                }
            }
            if (add) {
                ret.add(t);
            }
        }
        this.allergyRepository.flush();
        return ret;
    }

    /**
     * removes an allergy with a specific id.
     *
     * @param id the id of the allergy to remove.
     * @return true if deleted/false if couldn't.
     */
    public boolean removeAllergyById(Long id) {
        try {
            if (getAllergyById(id).isEmpty()) {
                return false;
            }
            this.allergyRepository.deleteAllergyById(id);
            this.allergyRepository.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}



