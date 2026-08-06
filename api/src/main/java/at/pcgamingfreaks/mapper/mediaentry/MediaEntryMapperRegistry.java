package at.pcgamingfreaks.mapper.mediaentry;

import at.pcgamingfreaks.model.db.media.MediaEntry;
import at.pcgamingfreaks.model.enums.MediaSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MediaEntryMapperRegistry {
	private final Map<MediaSource, MediaEntryMapper<? extends MediaEntry>> mappers;

	public MediaEntryMapperRegistry(List<MediaEntryMapper<? extends MediaEntry>> mappers) {
		this.mappers = mappers.stream().collect(Collectors.toMap(MediaEntryMapper::getSource, Function.identity()));
	}

	@SuppressWarnings("unchecked")
	public <E extends MediaEntry> MediaEntryMapper<E> getMapper(MediaSource source) {
		MediaEntryMapper<?> mapper = mappers.get(source);
		if (mapper == null) throw new IllegalArgumentException("No mapper found for source " + source);
		return (MediaEntryMapper<E>) mapper;
	}
}
