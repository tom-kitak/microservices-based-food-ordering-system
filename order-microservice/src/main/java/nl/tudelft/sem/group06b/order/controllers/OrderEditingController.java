package nl.tudelft.sem.group06b.order.controllers;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.model.Identification;
import nl.tudelft.sem.group06b.order.model.editing.AddPizzaRequestModel;
import nl.tudelft.sem.group06b.order.model.editing.AddPizzaResponseModel;
import nl.tudelft.sem.group06b.order.model.editing.AddToppingRequestModel;
import nl.tudelft.sem.group06b.order.model.editing.AddToppingResponseModel;
import nl.tudelft.sem.group06b.order.model.editing.OrderPizza;
import nl.tudelft.sem.group06b.order.model.editing.RemovePizzaRequestModel;
import nl.tudelft.sem.group06b.order.model.editing.RemoveToppingRequestModel;
import nl.tudelft.sem.group06b.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderEditingController {

    private final transient AuthManager authManager;

    private final transient OrderService orderService;

    /**
     * Adds a pizza to an order.
     *
     * @param request the request
     * @return the result
     */
    @PostMapping("/add_pizza")
    public ResponseEntity<AddPizzaResponseModel> addPizza(@RequestBody AddPizzaRequestModel request) {
        try {
            return ResponseEntity.ok(new AddPizzaResponseModel(
                    orderService.addPizza(authManager.getToken(), authManager.getMemberId(),
                            request.getOrderId(), request.getPizza())));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Removes a pizza from an order.
     *
     * @param request the request
     * @return the result
     */
    @DeleteMapping("/remove_pizza")
    public ResponseEntity<?> removePizza(@RequestBody RemovePizzaRequestModel request) {
        try {
            orderService.removePizza(request.getOrderId(), request.getPizza());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Adds a topping to a pizza.
     *
     * @param request the request
     * @return the result
     */
    @PostMapping("/add_topping")
    public ResponseEntity<AddToppingResponseModel> addTopping(@RequestBody AddToppingRequestModel request) {
        try {
            return ResponseEntity.ok(new AddToppingResponseModel(
                    orderService.addTopping(new Identification(authManager.getToken(), authManager.getMemberId()),
                            new OrderPizza(request.getOrderId(), request.getPizza()), request.getToppingId())));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Removes a topping from a pizza.
     *
     * @param request the request
     * @return the result
     */
    @DeleteMapping("/remove_topping")
    public ResponseEntity<?> removeTopping(@RequestBody RemoveToppingRequestModel request) {
        try {
            orderService.removeTopping(request.getOrderId(), request.getPizza(), request.getToppingId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }
}
