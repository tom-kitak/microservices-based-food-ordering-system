package nl.tudelft.sem.group06b.store.database;

import nl.tudelft.sem.group06b.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, String> {

}
