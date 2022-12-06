package nl.tudelft.sem.template.order.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;
import java.math.*;



@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(name = "customerId")
    private int customerId;

    @Column(name = "adminId")
    private int adminId;

    @Column(name = "pizzas")
    @ElementCollection
    private List<Pizza> pizzas;

    @Column(name = "completionTime")
    private Date completionTime;

    @Column(name = "status")
    private Status status;

    @Column(name = "couponsIds")
    @ElementCollection
    private List<Integer> couponsIds;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "storeId")
    private int storeId;

    /**
     * Instantiates a new Order.
     *
     * @param id ID of the order
     * @param customerId ID of the customer
     * @param adminId ID of the admin
     * @param pizzas list of pizzas in the order
     * @param completionTime time when the order is completed
     * @param status current status of the order
     * @param couponsIds all the coupons applied to the order
     * @param price price of the order
     * @param storeId store of the order
     */
    public Order(int id, int customerId, int adminId, List<Pizza> pizzas, Date completionTime, Status status, List<Integer> couponsIds, BigDecimal price, int storeId) {
        this.id = id;
        this.customerId = customerId;
        this.adminId = adminId;
        this.pizzas = pizzas;
        this.completionTime = completionTime;
        this.status = status;
        this.couponsIds = couponsIds;
        this.price = price;
        this.storeId = storeId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    public Date getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Integer> getCouponsIds() {
        return couponsIds;
    }

    public void setCouponsIds(List<Integer> couponsIds) {
        this.couponsIds = couponsIds;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
