package nl.tudelft.sem.group06b.order.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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

    @Column(name = "memberId")
    private String memberId;

    @Column(name = "pizzasIds")
    @ElementCollection
    private List<Long> pizzasIds;

    @Column(name = "selectedTime", nullable = false)
    private String selectedTime;

    @Column(name = "status")
    private Status status;

    @Column(name = "couponsIds")
    @ElementCollection
    private List<String> couponsIds;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "storeId", nullable = false)
    private Long storeId;

    /**
     * Instantiates a new Order.
     *
     * @param memberId ID of the member placing the order
     * @param pizzasIds IDs of pizzas in the order
     * @param selectedTime selected time of the order
     * @param status current status of the order
     * @param couponsIds IDs of the coupons of the order
     * @param price price of the order
     * @param storeId ID of the store of the order
     */
    public Order(String memberId, List<Long> pizzasIds, String selectedTime, Status status, List<String> couponsIds, BigDecimal price, Long storeId) {
        this.memberId = memberId;
        this.pizzasIds = pizzasIds;
        this.selectedTime = selectedTime;
        this.status = status;
        this.couponsIds = couponsIds;
        this.price = price;
        this.storeId = storeId;
    }

    public Long getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<Long> getPizzasIds() {
        return pizzasIds;
    }

    public void setPizzasIds(List<Long> pizzasIds) {
        this.pizzasIds = pizzasIds;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
