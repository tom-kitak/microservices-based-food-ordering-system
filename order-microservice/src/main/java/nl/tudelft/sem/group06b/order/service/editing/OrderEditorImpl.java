package nl.tudelft.sem.group06b.order.service.editing;

import java.util.ArrayList;
import java.util.Collection;
import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.Allergen;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderEditorImpl implements OrderEditor {

    private final transient OrderRepository orderRepository;
    private final transient MenuCommunication menuCommunication;
    private final transient StoreCommunication storeCommunication;
    private final transient CouponCommunication couponCommunication;

    private final transient String invalidMemberId = "Invalid member ID";
    private final transient String invalidOrderId = "Invalid order ID";
    private final transient String invalidToken = "Invalid token";
    private final transient String noActiveOrderMessage = "No active order with this ID";

    /**
     * Instantiates a new OrderEditorImpl.
     *
     * @param orderRepository repository of Orders
     */
    public OrderEditorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.menuCommunication = new MenuCommunication();
        this.storeCommunication = new StoreCommunication();
        this.couponCommunication = new CouponCommunication();
    }

    @Override
    public Collection<Allergen> addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception {
        if (token == null) {
            throw new Exception(invalidToken);
        } else if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (orderRepository.getOne(orderId) == null
                || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        } else if (pizza == null) {
            throw new Exception("Please enter valid pizza");
        } else if (memberId == null) {
            throw new Exception(invalidMemberId);
        }
        Collection<Allergen> allergensResponse = new ArrayList<>();

        // Query the Menu to see if pizza is valid
        menuCommunication.validatePizza(pizza, token);

        // Query the Menu to see if pizza contains an allergen and store the response to inform the user
        String responseMessage = menuCommunication.containsAllergen(pizza, memberId, token);
        if (responseMessage != null && !responseMessage.equals("")) {
            allergensResponse.add(new Allergen(responseMessage));
        }

        Order order = orderRepository.getOne(orderId);
        order.getPizzas().add(pizza);
        orderRepository.save(order);

        return allergensResponse;
    }

    @Override
    public void removePizza() {

    }

    @Override
    public void addTopping() {

    }

    @Override
    public void removeTopping() {

    }
}
