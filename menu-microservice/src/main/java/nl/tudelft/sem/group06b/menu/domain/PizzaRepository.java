package nl.tudelft.sem.group06b.menu.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    Optional<Pizza> getPizzaById(Long id);

    List<Pizza> getAllPizzas();
}
