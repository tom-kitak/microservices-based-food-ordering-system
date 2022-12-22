package nl.tudelft.sem.group06b.order.service.processor;

import java.util.ArrayList;
import java.util.Collection;

import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import nl.tudelft.sem.group06b.order.util.TimeValidation;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessorImpl implements OrderProcessor {

    private final transient OrderRepository orderRepository;

    private final transient int deadlineOffset = 30;
    private final transient String invalidMemberId = "Invalid member ID";
    private final transient String invalidOrderId = "Invalid order ID";
    private final transient String invalidToken = "Invalid token";
    private final transient String noActiveOrderMessage = "No active order with this ID";

    public OrderProcessorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Long startOrder(String token, String memberId) throws Exception {
        if (token == null) {
            throw new Exception(invalidToken);
        } else if (memberId == null) {
            throw new Exception(invalidMemberId);
        }

        Order order = new Order();
        order.setStatus(Status.ORDER_ONGOING);
        order.setMemberId(memberId);
        orderRepository.save(order);

        return order.getId();
    }

    @Override
    public void setOrderTime(Long orderId, String selectedTime) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (selectedTime == null || selectedTime.equals("")){
            throw new Exception("Invalid selected time");
        } else if (orderRepository.getOne(orderId) == null || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        }

        TimeValidation timeValidation = new TimeValidation();
        if (!timeValidation.isTimeValid(selectedTime, deadlineOffset)) {
            throw new Exception("Selected time has to be at least " + deadlineOffset + " minutes in the future.");
        }

        Order order = orderRepository.getOne(orderId);
        order.setSelectedTime(selectedTime);
        orderRepository.save(order);
    }

    @Override
    public void setOrderLocation(String token, String location, Long orderId) throws Exception {
        if (token == null) {
            throw new Exception(invalidToken);
        } else if (location == null) {
            throw new Exception("Please enter a valid location");
        } else if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (orderRepository.getOne(orderId) == null || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        }

        // validate location for the store
        StoreCommunication storeCommunication = new StoreCommunication();
        storeCommunication.validateLocation(location, token);

        // get the new storedID of the store with provided location
        Long storeId = storeCommunication.getStoreIdFromLocation(location, token);

        Order order = orderRepository.getOne(orderId);
        order.setStoreId(storeId);
        order.setLocation(location);
        orderRepository.save(order);
    }

    @Override
    public void placeOrder() {

    }

    @Override
    public void cancelOrder() {

    }

    @Override
    public Order fetchOrder() {
        return null;
    }

    @Override
    public Collection<Order> fetchAllStoreOrders() {
        return new ArrayList<>();
    }

    @Override
    public Collection<Order> fetchAllOrders() {
        return new ArrayList<>();
    }
}
