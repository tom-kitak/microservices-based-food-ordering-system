package nl.tudelft.sem.group06b.menu.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * interface for the repository for the pizza.
 */
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    /**
     * finds a pizza with the given id.
     *
     * @param id of the pizza to get.
     * @return an optional value of the pizza.
     */
    Optional<Pizza> findPizzaById(Long id);

    /**
     * removes a pizza from the repository.
     *
     * @param id the id of the pizza to remove.
     */
    void deletePizzaById(Long id);


}
