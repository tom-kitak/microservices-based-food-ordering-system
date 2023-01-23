package nl.tudelft.sem.group06b.menu.service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.tudelft.sem.group06b.menu.authentication.AuthManager;
import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.AllergyRepository;
import nl.tudelft.sem.group06b.menu.domain.PizzaRepository;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import nl.tudelft.sem.group06b.menu.domain.ToppingRepository;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class MenuToppingService {

    private final transient AuthManager authManager;
    /**
     * repository for toppings.
     */
    private final transient ToppingRepository toppingRepository;

    private final transient AllergyRepository allergyRepository;

    private final transient MenuAllergyService menuAllergyService;

    private final transient String regionalManager =  "regional_manager";

    /**
     * Constructor for menu topping service.
     *
     * @param tr topping repo.
     * @param authManager authmanager.
     * @param ar allergy repo.
     * @param pr pizza repo.
     */
    public MenuToppingService(ToppingRepository tr, AuthManager authManager, AllergyRepository ar, PizzaRepository pr) {
        this.menuAllergyService = new MenuAllergyService(ar, pr, tr, authManager);
        this.allergyRepository = ar;
        this.toppingRepository = tr;
        this.authManager = authManager;
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
     * returns specific topping with the given id.
     *
     * @param id of the topping.
     * @return topping object that has the id.
     */
    public Optional<Topping> getToppingById(Long id) throws Exception {
        Optional<Topping> ret = this.toppingRepository.findToppingById(id);
        this.toppingRepository.flush();
        return ret;
    }

    /**
     * removes a topping with a specific id.
     *
     * @param id of the topping to remove.
     * @return true if removed/false if no topping with that id.
     */
    public boolean removeToppingById(Long id) throws Exception {
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
     * checks if an allergy has a topping.
     *
     * @param id the id of the topping.
     * @param allergies the allergies in strings.
     * @return string of allergies/empty if none.
     */
    public Optional<String> checkForAllergiesTopping(Long id, List<String> allergies) throws Exception {
        String ret = "";
        List<Allergy> allergyList = menuAllergyService.getAllergiesFromStrings(allergies);
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


}
