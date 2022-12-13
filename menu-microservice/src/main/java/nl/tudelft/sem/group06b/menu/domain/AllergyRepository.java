package nl.tudelft.sem.group06b.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    /**
     * returns specific allergy that has the given id.
     *
     * @param id of the allergy.
     * @return the allergy with the id.
     */
    Allergy getAllergyById(Long id);
}
