package gevorgyan.pkmn.entity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import gevorgyan.pkmn.models.EnergyType;

@Converter
public class EnergyTypeConverter implements AttributeConverter<EnergyType, String> {

    @Override
    public String convertToDatabaseColumn(EnergyType attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public EnergyType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return EnergyType.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}