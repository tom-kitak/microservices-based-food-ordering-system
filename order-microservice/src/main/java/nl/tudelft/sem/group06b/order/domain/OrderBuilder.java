package nl.tudelft.sem.group06b.order.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderBuilder implements Builder {

    private Long id;
    private String memberId;
    private List<Pizza> pizzas;
    private String orderTime;
    private Status status;
    private String appliedCoupon;
    private Set<String> coupons;
    private BigDecimal price;
    private Long storeId;
    private String location;

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    @Override
    public void removePizza(Pizza pizza) {
        pizzas.remove(pizza);
    }

    @Override
    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    @Override
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public void setOrderStatus(Status status) {
        this.status = status;
    }

    @Override
    public void setAppliedCoupon(String appliedCoupon) {
        this.appliedCoupon = appliedCoupon;
    }

    @Override
    public void addCoupon(String coupon) {
        coupons.add(coupon);
    }

    @Override
    public void removeCoupon(String coupon) {
        coupons.remove(coupon);
    }

    @Override
    public void setCoupons(Set<String> coupons) {
        this.coupons = coupons;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public void setOrderLocation(String location) {
        this.location = location;
    }

    @Override
    public void setOrderStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public void addTopping(Pizza pizza, Long toppingId) {
        pizzas.get(pizzas.indexOf(pizza)).getToppings().add(toppingId);
    }

    @Override
    public void removeTopping(Pizza pizza, Long toppingId) {
        pizzas.get(pizzas.indexOf(pizza)).getToppings().remove(toppingId);
    }

    @Override
    public Order build() {
        if (id == null) {
            return new Order(memberId, pizzas, orderTime, status, price, storeId, location, appliedCoupon, coupons);
        }
        return new Order(id, memberId, pizzas, orderTime, status, price, storeId, location, appliedCoupon, coupons);
    }
}
