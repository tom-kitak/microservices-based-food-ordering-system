package nl.tudelft.sem.group06b.order.model.processor;

import lombok.Data;
import nl.tudelft.sem.group06b.order.domain.Location;

@Data
public class SetOrderLocationRequestModel {
    private Long orderId;
    private Location location;
}
