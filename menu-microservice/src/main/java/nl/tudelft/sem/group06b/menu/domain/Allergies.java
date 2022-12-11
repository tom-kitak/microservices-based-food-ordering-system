package nl.tudelft.sem.group06b.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Allergies extends JpaRepository<Allergy, Long> {

    /**
     * returns specific allergy that has the given id.
     *
     * @param id of the allergy.
     * @return the allergy with the id.
     */
    Allergy getAllergyById(Long id);

    /**
     * gets all allergies in repository.
     *
     * @return list of allergies in the repository.
     */
    List<Allergies> getAllAllergies();
}
