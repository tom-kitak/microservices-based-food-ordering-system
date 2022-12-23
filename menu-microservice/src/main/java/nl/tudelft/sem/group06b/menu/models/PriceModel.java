package nl.tudelft.sem.group06b.menu.models;

import java.util.List;
import lombok.Data;

@Data
public class PriceModel {
    Long id;
    List<Long> toppingIds;
}
