package nl.tudelft.sem.group06b.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class SendEmailRequestModel {
    private Long storeId;
    private String email;
}
