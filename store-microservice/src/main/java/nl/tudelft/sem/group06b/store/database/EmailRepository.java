package nl.tudelft.sem.group06b.store.database;

import java.util.List;
import nl.tudelft.sem.group06b.store.domain.email.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    /**
     * Retrieve all dummy emails from a specific store.
     *
     * @param storeId The store id.
     * @return A list of dummy emails.
     */
    @Query("SELECT a FROM Email a WHERE a.store.id = ?1")
    List<Email> retrieveEmailsByStoreId(Long storeId);

}
