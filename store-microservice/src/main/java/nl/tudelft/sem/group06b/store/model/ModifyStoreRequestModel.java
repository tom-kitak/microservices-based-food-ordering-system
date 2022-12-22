package nl.tudelft.sem.group06b.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyStoreRequestModel {
    private String name;
    private String storeLocation;
    private String manager;
}
