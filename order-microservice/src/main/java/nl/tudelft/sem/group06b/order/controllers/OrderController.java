package nl.tudelft.sem.group06b.order.controllers;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.model.editing.AddPizzaRequestModel;
import nl.tudelft.sem.group06b.order.model.editing.AddPizzaResponseModel;
import nl.tudelft.sem.group06b.order.model.editing.AddToppingRequestModel;
import nl.tudelft.sem.group06b.order.model.editing.AddToppingResponseModel;
import nl.tudelft.sem.group06b.order.model.editing.RemovePizzaRequestModel;
import nl.tudelft.sem.group06b.order.model.editing.RemoveToppingRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.CancelOrderRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.FetchOrderRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.FetchOrderResponseModel;
import nl.tudelft.sem.group06b.order.model.processor.FetchOrdersResponseModel;
import nl.tudelft.sem.group06b.order.model.processor.FetchStoreOrdersRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.FetchStoreOrdersResponseModel;
import nl.tudelft.sem.group06b.order.model.processor.PlaceOrderRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.PlaceOrderResponseModel;
import nl.tudelft.sem.group06b.order.model.processor.SetOrderLocationRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.SetOrderTimeRequestModel;
import nl.tudelft.sem.group06b.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/order")
public class OrderController {

    private final transient AuthManager authManager;

    private final transient OrderService orderService;

    /**
     * Starts an empty order.
     *
     * @return the outcome response
     */
    @PostMapping("/start")
    public ResponseEntity<?> startOrder() {
        try {
            return ResponseEntity.ok(orderService.startOrder(authManager.getMemberId()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Javadoc.
     *
     * @param request the request
     * @return the result
     */
    @PutMapping("/change_time")
    public ResponseEntity<?> setOrderTime(@RequestBody SetOrderTimeRequestModel request) {
        try {
            orderService.setOrderTime(request.getOrderId(), request.getOrderTime());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Javadoc.
     *
     * @param request the request
     * @return the result
     */
    @PutMapping("/change_location")
    public ResponseEntity<?> setOrderLocation(@RequestBody SetOrderLocationRequestModel request) {
        try {
            orderService.setOrderLocation(authManager.getToken(), request.getOrderId(), request.getLocation());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Javadoc.
     *
     * @param request the request
     * @return the result
     */
    @PostMapping("/place")
    public ResponseEntity<PlaceOrderResponseModel> placeOrder(@RequestBody PlaceOrderRequestModel request) {
        try {
            return ResponseEntity.ok(new PlaceOrderResponseModel(
                    orderService.placeOrder(authManager.getToken(), request.getOrderId())));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Javadoc.
     *
     * @param request the request
     * @return the result
     */
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestBody CancelOrderRequestModel request) {
        try {
            orderService.cancelOrder(authManager.getToken(), authManager.getMemberId(),
                                     authManager.getRole(), request.getOrderId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Javadoc.
     *
     * @param request the request
     * @return the result
     */
    @GetMapping("/fetch_single")
    public ResponseEntity<FetchOrderResponseModel> fetchOrder(@RequestBody FetchOrderRequestModel request) {
        try {
            return ResponseEntity.ok(new FetchOrderResponseModel(orderService.fetchOrder(request.getOrderId())));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Javadoc.
     *
     * @param request the request
     * @return the result
     */
    @GetMapping("/fetch_store")
    public ResponseEntity<FetchStoreOrdersResponseModel> fetchStoreOrders(
            @RequestBody FetchStoreOrdersRequestModel request) {
        try {
            return ResponseEntity.ok(new FetchStoreOrdersResponseModel(
                    orderService.fetchAllStoreOrders(request.getStoreId())));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Javadoc.
     *
     * @return the result
     */
    @GetMapping("/fetch_all")
    public ResponseEntity<FetchOrdersResponseModel> fetchOrders() {
        try {
            return ResponseEntity.ok(new FetchOrdersResponseModel(orderService.fetchAllOrders()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Javadoc.
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
     * Javadoc.
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
     * Javadoc.
     *
     * @param request the request
     * @return the result
     */
    @PostMapping("/add_topping")
    public ResponseEntity<AddToppingResponseModel> addTopping(@RequestBody AddToppingRequestModel request) {
        try {
            return ResponseEntity.ok(new AddToppingResponseModel(
                    orderService.addTopping(authManager.getToken(), authManager.getMemberId(),
                                            request.getOrderId(), request.getPizza(), request.getToppingId())));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Javadoc.
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


