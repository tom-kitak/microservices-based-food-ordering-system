package nl.tudelft.sem.group06b.order.controllers;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group06b.order.authentication.AuthManager;
import nl.tudelft.sem.group06b.order.model.processor.SetOrderLocationRequestModel;
import nl.tudelft.sem.group06b.order.model.processor.SetOrderTimeRequestModel;
import nl.tudelft.sem.group06b.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class LogisticsController {

    private final transient AuthManager authManager;

    private final transient OrderService orderService;

    /**
     * Changes the time of the order.
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
     * Changes the location of the order.
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
}
