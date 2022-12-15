package nl.tudelft.sem.group06b.order.controllers;

import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.OrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class OrderController {

//    private final transient AuthManager authManager;
//
//    private final transient OrderProcessor orderProcessor;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     * @param orderProcessor order processor
     */
//    @Autowired
//    public OrderController(AuthManager authManager, OrderProcessor orderProcessor) {
//        this.authManager = authManager;
//        this.orderProcessor = orderProcessor;
//    }

    /**
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello");

    }

//    @PostMapping("/placeOrder")
//    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
//
//    }

}
