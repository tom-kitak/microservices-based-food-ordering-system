package nl.tudelft.sem.group06b.order.controllers;

import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.OrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

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
        try {
            String selectedTime = order.getSelectedTime();
            Long storeId = order.getStoreId();
            String response = orderProcessor.startOrder(storeId, selectedTime);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/changeSelectedTime")
    public ResponseEntity<String> changeSelectedTime(@RequestBody Order order) {
        try {
            String selectedTime = order.getSelectedTime();
            Long orderId = order.getId();
            String response = orderProcessor.changeSelectedTime(orderId, selectedTime);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/changeSelectedLocation")
    public ResponseEntity<String> changeSelectedLocation(@RequestBody Order order) {

        try {
            Long storeId = order.getStoreId();
            Long orderId = order.getId();
            String response = orderProcessor.changeSelectedLocation(orderId, storeId);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/addPizzas")
    public ResponseEntity<String> addPizzas(@RequestBody Order order) {

        try {
            Long orderId = order.getId();
            List<Long> pizzasIds = order.getPizzasIds();
            String response = orderProcessor.addPizzas(orderId, pizzasIds);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/addCoupons")
    public ResponseEntity<String> addCoupons(@RequestBody Order order) {
        try {
            Long orderId = order.getId();
            List<String> couponsIds = order.getCouponsIds();
            String response = orderProcessor.addCoupons(orderId, couponsIds);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        try {
            Long orderId = order.getId();
            String response = orderProcessor.placeOrder(orderId);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/cancelOrder")
    public ResponseEntity<String> cancelOrder(@RequestBody Order order) {
        try {
            Long orderId = order.getId();
            String response = orderProcessor.cancelOrder(orderId);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<Order>> getAllOrders() {

        //Only admins and regional managers can view placed orders
        if (authManager.getRole().toLowerCase(Locale.ROOT).equals("customer")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Customers can't view orders");
        }

        try {
            List<Order> ordersInRepository = orderProcessor.getOrderRepository().findAll();
            return new ResponseEntity<>(ordersInRepository, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}


