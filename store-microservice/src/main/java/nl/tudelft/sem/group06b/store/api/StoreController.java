package nl.tudelft.sem.group06b.store.api;

import java.util.List;
import nl.tudelft.sem.group06b.store.domain.Location;
import nl.tudelft.sem.group06b.store.domain.Store;
import nl.tudelft.sem.group06b.store.model.LocationRequestModel;
import nl.tudelft.sem.group06b.store.model.ModifyStoreRequestModel;
import nl.tudelft.sem.group06b.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final transient StoreService storeService;

    private transient Long storeId;


    /**
     * Instantiates a new controller.
     *
     * @param storeService The store service.
     */
    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * Get a list of all pizza stores.
     *
     * @return A list of pizza stores.
     */
    @GetMapping("/showAllStores")
    public ResponseEntity<List<Store>> queryAllStores() {
        return ResponseEntity.ok(storeService.queryAllStores());
    }

    /**
     * Add a single store in the database.
     *
     * @param request serialized Store object in the form of a JSON in the request body
     * @return an HTTP response (200 if the store is saved, 400 otherwise)
     */
    @PostMapping("/addStore")
    public ResponseEntity<String> addStore(@RequestBody ModifyStoreRequestModel request) {

        try {
            String name = request.getName();
            Location location = new Location(request.getStoreLocation());
            storeService.addStore(name, location);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok("Store added!");
    }

    /**
     * Remove a single store from the database.
     *
     * @param storeId the store id.
     * @return an HTTP response (200 if the store is removed, 400 otherwise)
     */
    @DeleteMapping("/removeStore/{storeId}")
    public ResponseEntity<String> removeStore(@PathVariable Long storeId) {

        try {
            storeService.removeStore(storeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok("Store removed!");
    }

    /**
     * Validates if the store locations.
     *
     * @param request serialized Location object in the form of a JSON in the request body.
     * @return True if the given store location exists.
     */
    @GetMapping("/validateLocation")
    public ResponseEntity<Boolean> validateLocation(@RequestBody LocationRequestModel request) {
        Location location = new Location(request.getStoreLocation());
        return ResponseEntity.ok(storeService.validateStoreLocation(location));
    }

    /**
     * Gets the store id from the database.
     *
     * @param request serialized Location object in the form of a JSON in the request body.
     * @return The store id.
     */
    @GetMapping("/getStoreId")
    public ResponseEntity<Long> getStoreId(@RequestBody LocationRequestModel request) {
        try {
            Location location = new Location(request.getStoreLocation());
            storeId = storeService.retrieveStoreId(location);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.ok(storeId);
    }

}
