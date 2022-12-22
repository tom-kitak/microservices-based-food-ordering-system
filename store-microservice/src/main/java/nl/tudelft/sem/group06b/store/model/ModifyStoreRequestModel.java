package nl.tudelft.sem.group06b.store.model;

import lombok.Data;

@Data
public class ModifyStoreRequestModel {
    private String name;
    private String storeLocation;
    private String manager;
}
