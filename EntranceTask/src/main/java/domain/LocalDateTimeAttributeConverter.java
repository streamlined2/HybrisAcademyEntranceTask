package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, String> {

	@Override
	public String convertToDatabaseColumn(LocalDateTime attribute) {
		if(attribute == null ) return null;
		return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(attribute);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(String dbData) {
		if(dbData == null) return null;
		return (LocalDateTime) DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(dbData);
	}

}
