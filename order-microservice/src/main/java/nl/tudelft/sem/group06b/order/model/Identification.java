package nl.tudelft.sem.group06b.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identification {
    private String token;
    private String memberId;
}
