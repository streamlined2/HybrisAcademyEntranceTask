package domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class OrderStatusAttributeConverter implements AttributeConverter<Order.Status, String> {

	@Override
	public String convertToDatabaseColumn(Order.Status attribute) {
		if(attribute == null) return null;
		return attribute.name();
	}

	@Override
	public Order.Status convertToEntityAttribute(String dbData) {
		if(dbData == null) return null;
		return Enum.valueOf(Order.Status.class, dbData.strip());
	}

}
