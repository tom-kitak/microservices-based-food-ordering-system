package nl.tudelft.sem.group06b.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import nl.tudelft.sem.group06b.menu.authentication.AuthManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * MenuService gets toppings/pizzas from repositories.
 */
@Service
@Transactional
public class MenuService {
    /**
     * repository for pizzas.
     */
    private final transient PizzaRepository pizzaRepository;

    private final transient AuthManager authManager;
    /**
     * repository for toppings.
     */
    private final transient ToppingRepository toppingRepository;

    private final transient AllergyRepository allergyRepository;

    private final transient String regionalManager =  "regional_manager";

    /**
     * constructor for menuservice.
     *
     * @param ar repository for allergies
     * @param pr repository for pizzas.
     * @param tr repository for toppings.
     */
    public MenuService(AuthManager authManager, PizzaRepository pr, ToppingRepository tr, AllergyRepository ar) {
        this.authManager = authManager;
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
     * removes a topping with a specific id.
     *
     * @param id of the topping to remove.
     * @return true if removed/false if no topping with that id.
     */
    public boolean removeToppingById(Long id) {
        if (!authManager.getRole().equals(regionalManager)) {
            return false;
        }

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
     */
    public boolean addTopping(Topping t) {
        if (!authManager.getRole().equals(regionalManager)) {
            return false;
        }
        try {
            if (this.toppingRepository.findToppingById(t.getId()).isPresent()) {
                return false;
            }
            for (Allergy a : t.getAllergies()) {
                Optional<Allergy> optAll = this.allergyRepository.findAllergyById(a.getId());
                if (optAll.isEmpty()) {
                    return false;
                }
            }
            this.toppingRepository.save(t);
            this.toppingRepository.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * adds a pizza to the repository.
     *
     * @param p the pizza to add.
     * @return true if saved/false if there is a pizza with that id.
     */
    public boolean addPizza(Pizza p) {
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
     * adds an allergy to the repository.
     *
     * @param a the allergy to be added.
     * @return true if added/false if allergy exists already.
     */
    public boolean addAllergy(Allergy a) {
        if (!authManager.getRole().equals(regionalManager)) {
            return false;
        }
        try {
            if (getAllergyById(a.getId()).isPresent()) {
                return false;
            }
            this.allergyRepository.save(a);
            this.allergyRepository.flush();
            return true;
        } catch (IllegalArgumentException e) {
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
                    ret += "Pizza name: " + currPizza.get().getName() + "; ";
                }
            }
        }

        for (Long l : toppingList) {
            Optional<String> s = checkForAllergiesTopping(l, allergies);
            if (s.isPresent()) {
                ret += "Topping: " + s.get();
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
                    ret += curr.get().containsAllergy(a).get() + "; ";
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
        if (!authManager.getRole().equals(regionalManager)) {
            return false;
        }
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

    /**
     * returns a list of allergens for a specific user.
     *
     * @param l the user id.
     * @return list of allergies in a string format.
     */
    public List<String> getAllergens(String l) {
        try {
            String url = "http://localhost:8082/user/" + l + "/getAllergens";

            final RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", String.format("Bearer %s", authManager.getToken()));

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            return (List<String>) response.getBody().stream()
                    .map(x -> x.toString().split("=")[1].split("}")[0]).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}



