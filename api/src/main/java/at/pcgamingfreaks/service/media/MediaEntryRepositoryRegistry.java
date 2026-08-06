package at.pcgamingfreaks.service.media;

import at.pcgamingfreaks.model.db.media.MediaEntry;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.repo.AnilistMediaEntryRepository;
import at.pcgamingfreaks.model.repo.MediaEntryRepository;
import at.pcgamingfreaks.model.repo.SteamMediaEntryRepository;
import at.pcgamingfreaks.model.repo.TraktMediaEntryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MediaEntryRepositoryRegistry {

	private final AnilistMediaEntryRepository anilistMediaEntryRepository;
	private final TraktMediaEntryRepository traktMediaEntryRepository;
	private final SteamMediaEntryRepository steamMediaEntryRepository;

	private Map<MediaSource, MediaEntryRepository<?>> repositories;

	@PostConstruct
	void setup() {
		repositories = Map.of(
				MediaSource.ANILIST, anilistMediaEntryRepository,
				MediaSource.TRAKT, traktMediaEntryRepository,
				MediaSource.STEAM, steamMediaEntryRepository
		);
	}

	@SuppressWarnings("unchecked")
	public <E extends MediaEntry> MediaEntryRepository<E> getRepository(MediaSource source) {
		MediaEntryRepository<?> repo = repositories.get(source);
		if (repo == null) throw new IllegalArgumentException("No repository found for source: " + source);
		return (MediaEntryRepository<E>) repo;
	}
}
