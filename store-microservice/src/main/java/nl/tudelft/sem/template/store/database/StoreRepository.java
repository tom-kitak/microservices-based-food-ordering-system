package nl.tudelft.sem.template.store.database;

import nl.tudelft.sem.template.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, String> {

}
