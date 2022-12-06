package nl.tudelft.sem.group06b.store.api;

import java.util.List;
import nl.tudelft.sem.group06b.store.authentication.AuthManager;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class StoreController {

    private final transient AuthManager authManager;
    private final transient StoreRepository storeRepository;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public StoreController(AuthManager authManager, StoreRepository storeRepository) {
        this.authManager = authManager;
        this.storeRepository = storeRepository;
    }

    /**
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello " + authManager.getUsername());

    }

    /**
     * Get a list of all pizza stores.
     *
     * @return A list of pizza stores.
     */
    @GetMapping("/stores")
    public ResponseEntity<List<Store>> queryAllStores() {
        return ResponseEntity.ok(storeRepository.findAll());
    }

    /**
     * Imports all stores to the database locally.
     *
     * @param path An override to the default directory for activities.
     * @return A response to the request.
     */
    @PutMapping("/import")
    public ResponseEntity<Void> importStores(@RequestBody String path) {
        //TODO: import the stores into database
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Put a single store in the database.
     *
     * @param serialStore the serialized Store object in the form of a JSON in the request body
     * @return an HTTP response (200 if the store is saved, 400 otherwise)
     */
    @PutMapping("/addStore")
    public ResponseEntity<String> putStore(@RequestBody String serialStore) {
        //TODO: put a single store into database
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
