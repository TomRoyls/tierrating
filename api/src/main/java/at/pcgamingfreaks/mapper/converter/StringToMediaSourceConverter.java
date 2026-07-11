package at.pcgamingfreaks.mapper.converter;

import at.pcgamingfreaks.model.enums.MediaSource;
import org.springframework.core.convert.converter.Converter;

public class StringToMediaSourceConverter implements Converter<String, MediaSource> {
	@Override
	public MediaSource convert(String source) {
		return MediaSource.from(source.toUpperCase());
	}
}
