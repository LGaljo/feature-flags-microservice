package si.uni.lj.fri.lg0775.entities.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class UUIDAttributeConverter implements AttributeConverter<UUID, Object> {
    @Override
    public Object convertToDatabaseColumn(UUID attribute) {
        return attribute;
    }

    @Override
    public UUID convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        } else if (dbData instanceof UUID) {
            return (UUID) dbData;
        }

        return UUID.fromString(dbData.toString());
    }
}
