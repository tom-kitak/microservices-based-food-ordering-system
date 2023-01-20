package nl.tudelft.sem.group06b.order.service.editing;

import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.Allergens;
import nl.tudelft.sem.group06b.order.domain.Builder;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.OrderBuilder;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.model.Identification;
import nl.tudelft.sem.group06b.order.model.editing.OrderPizza;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public OrderEditorImpl(OrderRepository orderRepository, MenuCommunication menuCommunication,
                           StoreCommunication storeCommunication, CouponCommunication couponCommunication) {
        this.orderRepository = orderRepository;
        this.menuCommunication = menuCommunication;
        this.storeCommunication = storeCommunication;
        this.couponCommunication = couponCommunication;
    }

    @Override
    public Allergens addPizza(String token, String memberId, Long orderId, Pizza pizza) throws Exception {
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

        Allergens allergens = new Allergens("No allergens");

        // Query the Menu to see if pizza is valid
        menuCommunication.validatePizza(pizza, token);

        // Query the Menu to see if pizza contains allergens and store the response to inform the user
        String responseMessage = menuCommunication.containsAllergen(pizza, memberId, token);
        if (responseMessage != null && !responseMessage.equals("")) {
            allergens.setAllergensContent(responseMessage);
        }

        Order order = orderRepository.getOne(orderId);
        OrderBuilder orderBuilder = Builder.toBuilder(order);
        orderBuilder.addPizza(pizza);

        Order newOrder = orderBuilder.build();
        orderRepository.save(newOrder);

        return allergens;
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
        OrderBuilder orderBuilder = Builder.toBuilder(order);
        orderBuilder.removePizza(pizza);

        Order newOrder = orderBuilder.build();
        orderRepository.save(newOrder);
    }

    @Override
    public Allergens addTopping(Identification identification, OrderPizza orderPizza, Long toppingId) throws Exception {
        if (orderPizza.getOrderId() == null) {
            throw new Exception(invalidOrderId);
        }

        if (orderRepository.getOne(orderPizza.getOrderId()) == null
                || orderRepository.getOne(orderPizza.getOrderId()).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        }

        if (toppingId == null) {
            throw new Exception("Invalid topping");
        }

        if (orderPizza.getPizza() == null) {
            throw new Exception(invalidPizza);
        }

        if (identification.getToken() == null) {
            throw new Exception(invalidToken);
        }

        Allergens allergens = new Allergens("No allergens");

        // Query the Menu to see if topping is valid
        menuCommunication.validateTopping(toppingId, identification.getToken());

        // Query the Menu to see if topping contains allergens and store the response to inform the user
        String responseMessage = menuCommunication.containsAllergenTopping(toppingId, identification.getMemberId(),
                identification.getToken());
        if (responseMessage != null && !responseMessage.equals("")) {
            allergens.setAllergensContent(responseMessage);
        }

        Order order = orderRepository.getOne(orderPizza.getOrderId());
        if (!order.getPizzas().contains(orderPizza.getPizza())) {
            throw new Exception(invalidPizza);
        }
        OrderBuilder orderBuilder = Builder.toBuilder(order);
        orderBuilder.addTopping(orderPizza.getPizza(), toppingId);

        Order newOrder = orderBuilder.build();
        orderRepository.save(newOrder);

        return allergens;
    }

    @Override
    public void removeTopping(Long orderId, Pizza pizza, Long toppingId) throws Exception {
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

        OrderBuilder orderBuilder = Builder.toBuilder(order);
        orderBuilder.removeTopping(pizza, toppingId);

        Order newOrder = orderBuilder.build();
        orderRepository.save(newOrder);
    }
}
