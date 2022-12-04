package nl.tudelft.sem.group06b.authentication.domain.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the Username value object.
 */
@Converter
public class UsernameAttributeConverter implements AttributeConverter<Username, String> {

    @Override
    public String convertToDatabaseColumn(Username attribute) {
        return attribute.toString();
    }

    @Override
    public Username convertToEntityAttribute(String dbData) {
        return new Username(dbData);
    }

}

