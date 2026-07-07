package at.pcgamingfreaks.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum MediaSource {
	ANILIST,
	TRAKT,
	TMDB,
	STEAM;

	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static MediaSource from(String text) {
		for (MediaSource service : MediaSource.values()) {
			if (service.name().equalsIgnoreCase(text)) return service;
		}
		throw new IllegalArgumentException();
	}
}
