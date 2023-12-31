package nl.tudelft.sem.group06b.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Entity
@Data
@Table(name = "orders")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "memberId")
    private String memberId;

    @Column(name = "pizzas", length = 20000)
    @ElementCollection
    private List<Pizza> pizzas;

    @Column(name = "selectedTime")
    private String selectedTime;

    @Column(name = "status")
    private Status status;

    @Column(name = "coupon")
    private String appliedCoupon;

    @Column(name = "coupons")
    @ElementCollection
    private Set<String> coupons;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "storeId")
    private Long storeId;

    @Column(name = "location")
    private String location;

    /**
     * Instantiates a new Order.
     *
     * @param memberId ID of the member placing the order
     * @param pizzas pizzas of the order
     * @param selectedTime selected time of the order
     * @param status current status of the order
     * @param price price of the order
     * @param storeId ID of the store of the order
     * @param location location of the store
     */
    public Order(String memberId, List<Pizza> pizzas, String selectedTime,
                 Status status, BigDecimal price, Long storeId,
                 String location, String appliedCoupon, Set<String> coupons) {
        this.memberId = memberId;
        this.pizzas = pizzas;
        this.selectedTime = selectedTime;
        this.status = status;
        this.appliedCoupon = appliedCoupon;
        this.price = price;
        this.storeId = storeId;
        this.location = location;
        this.coupons = coupons;
    }

    /**
     * Instantiates a new Order.
     *
     * @param id ID of the order in case there is no need to autogenerate it
     * @param memberId ID of the member placing the order
     * @param pizzas pizzas of the order
     * @param orderTime selected time of the order
     * @param status current status of the order
     * @param price price of the order
     * @param storeId ID of the store of the order
     * @param location location of the store
     */
    public Order(Long id, String memberId, List<Pizza> pizzas, String orderTime,
                 Status status, BigDecimal price, Long storeId, String location,
                 String appliedCoupon, Set<String> coupons) {
        this.id = id;
        this.memberId = memberId;
        this.pizzas = pizzas;
        this.selectedTime = orderTime;
        this.status = status;
        this.appliedCoupon = appliedCoupon;
        this.price = price;
        this.storeId = storeId;
        this.location = location;
        this.coupons = coupons;
    }

    public Order(String memberId, Status status) {
        this.memberId = memberId;
        this.status = status;
    }

    /**
     * Calculates the total price of the pizzas currently in the Order.
     */
    public BigDecimal calculateTotalPrice() {
        BigDecimal priceSum = new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN);
        for (Pizza pizza : pizzas) {
            priceSum = priceSum.add(pizza.getPrice().setScale(2, RoundingMode.HALF_DOWN));
        }
        this.price = priceSum.setScale(2, RoundingMode.HALF_DOWN);
        return priceSum.setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * Formats order details into an email.
     *
     * @return formatted email
     */
    public String formatEmail() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order with an ID of " + this.id + " has been placed by " + this.memberId);
        sb.append(", to be collected at " + this.selectedTime + "\n");
        sb.append("Order contains the following pizzas:\n");
        sb.append(pizzas.toString() + "\n");
        if (appliedCoupon == null || appliedCoupon.isEmpty()) {
            sb.append("No coupon was applied");
        } else {
            sb.append("Following coupon was applied: " + appliedCoupon);
        }
        sb.append("Final price of the order is " + this.price);
        return sb.toString();
    }

    /**
     * Formats the receipt with Order details.
     *
     * @return formatted receipt
     */
    public String formatReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: " + this.id + "\n");
        sb.append("Date: " + this.selectedTime + "\n");
        sb.append("Location: " + this.location + "\n");
        sb.append("Pizzas: " + this.pizzas.toString() + "\n");
        sb.append("Final price: " + this.price);
        if (appliedCoupon.isEmpty()) {
            sb.append("No coupon was applied");
        } else {
            sb.append("Following coupon was applied: " + this.appliedCoupon);
        }

        return sb.toString();
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
