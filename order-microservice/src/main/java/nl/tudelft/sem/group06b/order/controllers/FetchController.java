package nl.tudelft.sem.group06b.order.controllers;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.model.processor.FetchOrderRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.FetchOrderResponseModel;
import nl.tudelft.sem.group06b.order.model.processor.FetchOrdersResponseModel;
import nl.tudelft.sem.group06b.order.model.processor.FetchStoreOrdersRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.FetchStoreOrdersResponseModel;
import nl.tudelft.sem.group06b.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class FetchController {

    private final transient AuthManager authManager;

    private final transient OrderService orderService;

    /**
     * Gets an order.
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
     * Gets all orders from a certain store.
     *
     * @param request the request
     * @return the result
     */
    @GetMapping("/fetch_store")
    public ResponseEntity<FetchStoreOrdersResponseModel> fetchStoreOrders(
            @RequestBody FetchStoreOrdersRequestModel request) {
        try {
            return ResponseEntity.ok(new FetchStoreOrdersResponseModel(
                    orderService.fetchAllStoreOrders(authManager.getToken(), authManager.getMemberId(),
                            authManager.getRole(), request.getStoreId())));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }

    /**
     * Gets all orders from all stores.
     *
     * @return the result
     */
    @GetMapping("/fetch_all")
    public ResponseEntity<FetchOrdersResponseModel> fetchOrders() {
        try {
            return ResponseEntity.ok(new FetchOrdersResponseModel(orderService.fetchAllOrders(
                    authManager.getToken(), authManager.getMemberId(), authManager.getRole())));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
    }
}
