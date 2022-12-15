package nl.tudelft.sem.group06b.menu.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository for the allergies.
 */
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    /**
     * returns specific allergy that has the given id.
     *
     * @param id of the allergy.
     * @return the allergy with the id or empty optional if doesn't exist.
     */
    Optional<Allergy> findAllergyById(Long id);
}
