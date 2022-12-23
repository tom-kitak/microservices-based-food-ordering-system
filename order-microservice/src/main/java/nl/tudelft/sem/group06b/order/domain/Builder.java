package nl.tudelft.sem.group06b.order.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface Builder {

    void setId(Long id);

    void setMemberId(String memberId);

    void addPizza(Pizza pizza);

    void removePizza(Pizza pizza);

    void setPizzas(List<Pizza> pizzas);

    void setOrderTime(String orderTime);

    void setOrderStatus(Status status);

    void setAppliedCoupon(String appliedCoupon);

    void addCoupon(String coupon);

    void removeCoupon(String coupon);

    void setCoupons(Set<String> coupons);

    void setPrice(BigDecimal price);

    void setOrderLocation(String location);

    void setOrderStoreId(Long storeId);

    void addTopping(Pizza pizza, Long toppingId);

    void removeTopping(Pizza pizza, Long toppingId);

    Order build();

    /**
     * Transforms an order object into its builder to allow element modification.
     *
     * @param order the order that will need to be edited
     * @return a builder that contains the same elements as order
     */
    static OrderBuilder toBuilder(Order order) {
        OrderBuilder orderBuilder = new OrderBuilder();
        orderBuilder.setId(order.getId());
        orderBuilder.setMemberId(order.getMemberId());
        orderBuilder.setPizzas(order.getPizzas());
        orderBuilder.setOrderTime(order.getSelectedTime());
        orderBuilder.setOrderStatus(order.getStatus());
        orderBuilder.setAppliedCoupon(order.getAppliedCoupon());
        orderBuilder.setCoupons(order.getCoupons());
        orderBuilder.setPrice(order.getPrice());
        orderBuilder.setOrderLocation(order.getLocation());
        orderBuilder.setOrderStoreId(order.getStoreId());
        return orderBuilder;
    }
}
