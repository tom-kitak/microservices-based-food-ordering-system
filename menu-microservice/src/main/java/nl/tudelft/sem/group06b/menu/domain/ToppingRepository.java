package nl.tudelft.sem.group06b.menu.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * repository for the toppings.
 */
public interface ToppingRepository extends JpaRepository<Topping, Long> {
    /**
     * gets a specific topping that has the given id.
     *
     * @param id the id of the topping to return.
     * @return the Optional value of the topping, blank if there is no topping with that id
     */
    @Query
    Optional<Topping> findToppingById(Long id);

    void deleteToppingById(Long id);
}
