package nl.tudelft.sem.group06b.authentication.domain.role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the RoleName value object.
 */
@Converter
public class RoleNameAttributeConverter implements AttributeConverter<RoleName, String> {

    @Override
    public String convertToDatabaseColumn(RoleName attribute) {
        return attribute.toString();
    }

    @Override
    public RoleName convertToEntityAttribute(String dbData) {
        return new RoleName(dbData);
    }

}