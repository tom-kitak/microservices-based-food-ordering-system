package nl.tudelft.sem.group06b.order.controllers;

import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.OrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class OrderController {

    private final transient AuthManager authManager;

    private final transient OrderProcessor orderProcessor;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     * @param orderProcessor order processor
     */
    @Autowired
    public OrderController(AuthManager authManager, OrderProcessor orderProcessor) {
        this.authManager = authManager;
        this.orderProcessor = orderProcessor;
    }

    @PostMapping("/startOrder")
    public ResponseEntity<String> startOrder(@RequestBody Order order) {
        String selectedTime = order.getSelectedTime();
        Long storeId = order.getStoreId();
        String response = orderProcessor.startOrder(storeId, selectedTime);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/changeSelectedTime")
    public ResponseEntity<String> changeSelectedTime(@RequestBody Order order) {
        String selectedTime = order.getSelectedTime();
        Long orderId = order.getId();
        String response = orderProcessor.changeSelectedTime(orderId, selectedTime);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // TODO
    // Add /changeSelectedLocation

    @PostMapping("/addPizzas")
    public ResponseEntity<String> addPizzas(@RequestBody Order order) {
        Long orderId = order.getId();
        List<Long> pizzasIds = order.getPizzasIds();
        String response = orderProcessor.addPizzas(orderId, pizzasIds);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/addCoupons")
    public ResponseEntity<String> addCoupons(@RequestBody Order order) {
        Long orderId = order.getId();
        List<String> couponsIds = order.getCouponsIds();
        String response = orderProcessor.addCoupons(orderId, couponsIds);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        Long orderId = order.getId();
        String response = orderProcessor.placeOrder(orderId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/cancelOrder")
    public ResponseEntity<String> cancelOrder(@RequestBody Order order) {
        Long orderId = order.getId();
        String response = orderProcessor.cancelOrder(orderId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<Order>> getAllOrders() {

        // TODO
        // authentication: make sure only admin can get a valid response

        List<Order> ordersInRepository = orderProcessor.getOrderRepository().findAll();

        return new ResponseEntity<>(ordersInRepository, HttpStatus.OK);
    }

}


