package at.pcgamingfreaks.mapper.converter;

import at.pcgamingfreaks.model.enums.MediaType;
import org.springframework.core.convert.converter.Converter;

public class StringToMediaTypeConverter implements Converter<String, MediaType> {
	@Override
	public MediaType convert(String source) {
		if (source == null || source.isBlank()) {
			throw new IllegalArgumentException("MediaType cannot be null or empty");
		}

		String normalized = source.replace("-", "_").toUpperCase();

		if (normalized.endsWith("S")) {
			normalized = normalized.substring(0, normalized.length() - 1);
		}

		try {
			return MediaType.valueOf(normalized);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unknown MediaType: " + source);
		}
	}
}
