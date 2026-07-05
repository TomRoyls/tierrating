package at.pcgamingfreaks.mapper.converter;

import at.pcgamingfreaks.model.enums.MediaType;
import org.springframework.core.convert.converter.Converter;

public class StringToContentTypeConverter implements Converter<String, MediaType> {
	@Override
	public MediaType convert(String source) {
		return MediaType.from(source.toUpperCase());
	}
}
