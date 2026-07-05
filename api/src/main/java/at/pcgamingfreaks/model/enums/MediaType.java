package at.pcgamingfreaks.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MediaType {
	ANIME,
	MANGA,
	MOVIES,
	TVSHOWS,
	TVSHOWS_SEASONS,
	GAMES;

	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static MediaType from(String text) {
		text = text.replace("-", "_");
		for (MediaType type : MediaType.values()) {
			if (type.name().equalsIgnoreCase(text)) return type;
		}
		throw new IllegalArgumentException();
	}
}
