package nl.tudelft.sem.group06b.menu.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * interface for the repository for the pizza.
 */
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    Optional<Pizza> findPizzaById(Long id);
}
