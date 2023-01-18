package nl.tudelft.sem.group06b.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import nl.tudelft.sem.group06b.menu.authentication.AuthManager;
import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.AllergyRepository;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import nl.tudelft.sem.group06b.menu.domain.PizzaRepository;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import nl.tudelft.sem.group06b.menu.domain.ToppingRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Transactional
@Service
public class MenuAllergyService {

    private final transient String regionalManager =  "regional_manager";

    private final transient AuthManager authManager;

    private final transient AllergyRepository allergyRepository;

    private final transient PizzaRepository pizzaRepository;

    private final transient ToppingRepository toppingRepository;

    /**
     * constructor for menuAllergyService.
     *
     * @param ar allergy repo.
     * @param pr pizza repo.
     * @param tr topping repo.
     * @param authManager authmanager.
     */
    public MenuAllergyService(AllergyRepository ar, PizzaRepository pr, ToppingRepository tr, AuthManager authManager) {
        this.toppingRepository = tr;
        this.pizzaRepository = pr;
        this.allergyRepository = ar;
        this.authManager = authManager;
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
        for (Topping t : toppingRepository.findAll()) {
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
        for (Pizza p : pizzaRepository.findAll()) {
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
