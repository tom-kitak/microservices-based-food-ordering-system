package nl.tudelft.sem.group06b.order.service.editing;

import java.util.ArrayList;
import java.util.Collection;
import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.AllergenResponse;
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
    private final transient String invalidPizza = "Invalid pizza";
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
    public AllergenResponse addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception {
        if (token == null) {
            throw new Exception(invalidToken);
        } else if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (orderRepository.getOne(orderId) == null
                || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        } else if (pizza == null) {
            throw new Exception(invalidPizza);
        } else if (memberId == null) {
            throw new Exception(invalidMemberId);
        }
        AllergenResponse allergenResponse = new AllergenResponse();

        // Query the Menu to see if pizza is valid
        menuCommunication.validatePizza(pizza, token);

        // Query the Menu to see if pizza contains allergens and store the response to inform the user
        String responseMessage = menuCommunication.containsAllergen(pizza, memberId, token);
        if (responseMessage != null && !responseMessage.equals("")) {
            allergenResponse.setAllergenContent(responseMessage);
        }

        Order order = orderRepository.getOne(orderId);
        order.getPizzas().add(pizza);
        orderRepository.save(order);

        return allergenResponse;
    }

    @Override
    public void removePizza(Long orderId, Pizza pizza) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (orderRepository.getOne(orderId) == null
                || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        } else if (pizza == null) {
            throw new Exception(invalidPizza);
        }

        Order order = orderRepository.getOne(orderId);
        order.getPizzas().remove(pizza);
        orderRepository.save(order);
    }

    @Override
    public AllergenResponse addTopping(String token, Long orderId, Long toppingId,
                                       Pizza pizza, String memberId) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (orderRepository.getOne(orderId) == null
                || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        } else if (toppingId == null) {
            throw new Exception("Invalid topping");
        } else if (pizza == null) {
            throw new Exception(invalidPizza);
        } else if (token == null) {
            throw new Exception(invalidToken);
        }
        AllergenResponse allergenResponse = new AllergenResponse();

        // Query the Menu to see if topping is valid
        menuCommunication.validateTopping(toppingId, token);

        // Query the Menu to see if topping contains allergens and store the response to inform the user
        String responseMessage = menuCommunication.containsAllergenTopping(toppingId, memberId, token);
        if (responseMessage != null && !responseMessage.equals("")) {
            allergenResponse.setAllergenContent(responseMessage);
        }

        Order order = orderRepository.getOne(orderId);
        order.getPizzas().get(order.getPizzas().indexOf(pizza)).getToppings().add(toppingId);
        orderRepository.save(order);

        return allergenResponse;
    }

    @Override
    public void removeTopping(Long orderId, Long toppingId, Pizza pizza) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (orderRepository.getOne(orderId) == null
                || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        } else if (toppingId == null) {
            throw new Exception("Invalid topping");
        } else if (pizza == null) {
            throw new Exception(invalidPizza);
        }

        Order order = orderRepository.getOne(orderId);

        if (!order.getPizzas().contains(pizza)) {
            throw new Exception(invalidPizza);
        }
        Pizza pizzaToEdit = order.getPizzas().remove(order.getPizzas().indexOf(pizza));
        pizzaToEdit.getToppings().remove(toppingId);
        order.getPizzas().add(pizzaToEdit);

        orderRepository.save(order);
    }
}
