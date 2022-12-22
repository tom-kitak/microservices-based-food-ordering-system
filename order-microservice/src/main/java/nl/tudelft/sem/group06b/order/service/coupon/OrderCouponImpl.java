package nl.tudelft.sem.group06b.order.service.coupon;

import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderCouponImpl implements OrderCoupon {

    private final transient OrderRepository orderRepository;
    private final transient MenuCommunication menuCommunication;
    private final transient StoreCommunication storeCommunication;
    private final transient CouponCommunication couponCommunication;

    private final transient String invalidMemberId = "Invalid member ID";
    private final transient String invalidOrderId = "Invalid order ID";
    private final transient String invalidToken = "Invalid token";
    private final transient String noActiveOrderMessage = "No active order with this ID";

    /**
     * Instantiates a new OrderCouponImpl.
     *
     * @param orderRepository repository of Orders
     */
    public OrderCouponImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.menuCommunication = new MenuCommunication();
        this.storeCommunication = new StoreCommunication();
        this.couponCommunication = new CouponCommunication();
    }

    @Override
    public void addCoupon(String token, Long orderId, String coupon) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderId);
        } else if (orderRepository.getOne(orderId) == null
                || orderRepository.getOne(orderId).getStatus() != Status.ORDER_ONGOING) {
            throw new Exception(noActiveOrderMessage);
        } else if (coupon == null) {
            throw new Exception("Please enter valid coupon");
        } else if (token == null) {
            throw new Exception(invalidToken);
        }

        // call to Coupon-microservice to see if coupon is valid
        couponCommunication.validateCoupon(coupon, token);

        Order order = orderRepository.getOne(orderId);
        order.getCouponsIds().add(coupon);
        orderRepository.save(order);
    }

    @Override
    public void removeCoupon() {

    }
}
