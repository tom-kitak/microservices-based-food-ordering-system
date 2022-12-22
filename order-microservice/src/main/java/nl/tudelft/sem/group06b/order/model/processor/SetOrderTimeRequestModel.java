package nl.tudelft.sem.group06b.order.model.processor;

import lombok.Data;

@Data
public class SetOrderTimeRequestModel {
    private Long orderId;
    private String orderTime;
}
