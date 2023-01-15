package nl.tudelft.sem.group06b.order.controllers;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.model.processor.CancelOrderRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.PlaceOrderRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.PlaceOrderResponseModel;
import nl.tudelft.sem.group06b.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderProcessingController {

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
     * Places an order.
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
     * Cancels an order.
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
}


