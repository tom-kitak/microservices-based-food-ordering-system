package nl.tudelft.sem.group06b.authentication.domain.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the Username value object.
 */
@Converter
public class MemberIdAttributeConverter implements AttributeConverter<MemberId, String> {

    @Override
    public String convertToDatabaseColumn(MemberId attribute) {
        return attribute.toString();
    }

    @Override
    public MemberId convertToEntityAttribute(String dbData) {
        return new MemberId(dbData);
    }

}

