package nl.tudelft.sem.group06b.menu.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository for the toppings.
 */
public interface ToppingRepository extends JpaRepository<Topping, Long> {
    /**
     * gets all the toppings from the repository.
     *
     * @return list of toppings in the repository.
     */
    List<Topping> getAllToppings();

    /**
     * gets a specific topping that has the given id.
     *
     * @param id the id of the topping to return.
     * @return the Optional value of the topping, blank if there is no topping with that id
     */
    Optional<Topping> getToppingById(Long id);
}
