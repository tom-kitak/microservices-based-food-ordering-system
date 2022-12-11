package nl.tudelft.sem.group06b.authentication.domain.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the Username value object.
 */
@Converter
public class MemberIDAttributeConverter implements AttributeConverter<MemberID, String> {

    @Override
    public String convertToDatabaseColumn(MemberID attribute) {
        return attribute.toString();
    }

    @Override
    public MemberID convertToEntityAttribute(String dbData) {
        return new MemberID(dbData);
    }

}

