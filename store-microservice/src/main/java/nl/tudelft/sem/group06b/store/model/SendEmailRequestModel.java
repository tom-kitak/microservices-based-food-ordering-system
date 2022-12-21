package nl.tudelft.sem.group06b.store.model;

import lombok.Data;


@Data
public class SendEmailRequestModel {
    private Long storeId;
    private String email;
}
