package nl.tudelft.sem.group06b.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "customerId")
    private Long customerId;

    @Column(name = "pizzas")
    @ElementCollection
    private List<Pizza> pizzas;

    @Column(name = "completionTime")
    private Date completionTime;

    @Column(name = "status")
    private Status status;

    @Column(name = "couponsIds")
    @ElementCollection
    private List<String> couponsIds;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "storeId")
    private String storeId;

    /**
     * Instantiates a new Order.
     *
     * @param customerId ID of the customer
     * @param pizzas list of pizzas in the order
     * @param completionTime time when the order is completed
     * @param status current status of the order
     * @param couponsIds all the coupons applied to the order
     * @param price price of the order
     * @param storeId store of the order
     */
    public Order(Long customerId, List<Pizza> pizzas, Date completionTime, Status status,
                 List<String> couponsIds, BigDecimal price, String storeId) {
        this.customerId = customerId;
        this.pizzas = pizzas;
        this.completionTime = completionTime;
        this.status = status;
        this.couponsIds = couponsIds;
        this.price = price;
        this.storeId = storeId;
    }

    /**
     * Returns the ID of the customer that placed the order.
     *
     * @return ID of the costumer
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Changes the costumer ID to another costumer.
     *
     * @param customerId ID of the new customer
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public List<String> getCouponsIds() {
        return couponsIds;
    }

    public void setCouponsIds(List<String> couponsIds) {
        this.couponsIds = couponsIds;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
