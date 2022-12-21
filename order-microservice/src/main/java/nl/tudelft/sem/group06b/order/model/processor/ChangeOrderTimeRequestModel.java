package nl.tudelft.sem.group06b.order.model.processor;

import java.util.Date;
import lombok.Data;

@Data
public class ChangeOrderTimeRequestModel {
    private Date orderTime;
}
