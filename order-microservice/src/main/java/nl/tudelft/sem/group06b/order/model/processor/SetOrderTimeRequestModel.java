package nl.tudelft.sem.group06b.order.model.processor;

import java.util.Date;
import lombok.Data;

@Data
public class SetOrderTimeRequestModel {
    private Long orderId;
    private Date orderTime;
}
