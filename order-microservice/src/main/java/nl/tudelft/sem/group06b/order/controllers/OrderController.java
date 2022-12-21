package nl.tudelft.sem.group06b.order.controllers;

import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.OrderProcessor;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final transient AuthManager authManager;

    private final transient OrderService orderService;

    private final transient OrderProcessor orderProcessor;

    /**
     * Starts an order.
     *
     * @param order The order requested
     * @return the outcome response
     */
    @PostMapping("/startOrder")
    public ResponseEntity<String> startOrder(@RequestBody Order order) {
        try {
            String selectedTime = order.getSelectedTime();
            String location = order.getLocation();
            String memberId = authManager.getMemberId();
            String token = authManager.getToken();
            String response = orderProcessor.startOrder(location, memberId, selectedTime, token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Changes selected time of an order.
     *
     * @param order The order requested
     * @return the outcome response
     */
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

    /**
     * Changes selected location of an order.
     *
     * @param order The order requested
     * @return the outcome response
     */
    @PostMapping("/changeSelectedLocation")
    public ResponseEntity<String> changeSelectedLocation(@RequestBody Order order) {
        try {
            String location = order.getLocation();
            Long orderId = order.getId();
            String token = authManager.getToken();
            String response = orderProcessor.changeSelectedLocation(location, orderId, token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Adds pizzas to an order.
     *
     * @param order The order requested
     * @return the outcome response
     */
    @PostMapping("/addPizzas")
    public ResponseEntity<String> addPizzas(@RequestBody Order order) {
        try {
            String memberId = order.getMemberId();
            Long orderId = order.getId();
            List<Pizza> pizzas = order.getPizzas();
            String token = authManager.getToken();
            String response = orderProcessor.addPizzas(memberId, orderId, pizzas, token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Adds coupons to an order.
     *
     * @param order The order requested
     * @return the outcome response
     */
    @PostMapping("/addCoupons")
    public ResponseEntity<String> addCoupons(@RequestBody Order order) {
        try {
            Long orderId = order.getId();
            List<String> couponsIds = order.getCouponsIds();
            String token = authManager.getToken();
            String response = orderProcessor.addCoupons(orderId, couponsIds, token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Places an order.
     *
     * @param order The order requested
     * @return the outcome response
     */
    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        try {
            Long orderId = order.getId();
            String token = authManager.getToken();
            String response = orderProcessor.placeOrder(orderId, token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Cancels an order.
     *
     * @param order The order requested
     * @return the outcome response
     */
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

    /**
     * Gets all placed orders.
     *
     * @return all currently placed orders
     */
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


