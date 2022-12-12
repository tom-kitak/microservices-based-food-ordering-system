package nl.tudelft.sem.group06b.store.api;

import java.util.List;
import nl.tudelft.sem.group06b.store.authentication.AuthManager;
import nl.tudelft.sem.group06b.store.database.StoreRepository;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.Store;
import nl.tudelft.sem.group06b.store.model.AddStoreRequestModel;
import nl.tudelft.sem.group06b.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final transient AuthManager authManager;
    private final transient StoreRepository storeRepository;
    private final transient StoreService storeService;


    /**
     * Instantiates a new controller.
     *
     * @param authManager The authentication manager
     * @param storeRepository The store repo.
     * @param storeService The store service.
     */
    @Autowired
    public StoreController(AuthManager authManager, StoreRepository storeRepository, StoreService storeService) {
        this.authManager = authManager;
        this.storeRepository = storeRepository;
        this.storeService = storeService;
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
    @GetMapping("/showAllStores")
    public ResponseEntity<List<Store>> queryAllStores() {
        return ResponseEntity.ok(storeRepository.findAll());
    }

    /**
     * Add a single store in the database.
     *
     * @param request serialized Store object in the form of a JSON in the request body
     * @return an HTTP response (200 if the store is saved, 400 otherwise)
     */
    @PostMapping("/addStore")
    public ResponseEntity addStore(@RequestBody AddStoreRequestModel request) {

        try {
            String name = request.getName();
            Location location = new Location(request.getLocation());
            storeService.addStore(name, location);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Remove a single store from the database.
     *
     * @param request serialized Store object in the form of a JSON in the request body
     * @return an HTTP response (200 if the store is removed, 400 otherwise)
     */
    @DeleteMapping("/removeStore")
    public ResponseEntity removeStore(@RequestBody AddStoreRequestModel request) {

        try {
            Location location = new Location(request.getLocation());
            storeService.removeStore(location);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
