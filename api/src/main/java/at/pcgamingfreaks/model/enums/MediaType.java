package at.pcgamingfreaks.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MediaType {
	ANIME,
	MANGA,
	MOVIE,
	TV_SHOW,
	TV_SHOW_SEASON,
	GAME;

	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static MediaType from(String text) {
		for (MediaType type : MediaType.values()) {
			if (type.name().equalsIgnoreCase(text)) return type;
		}
		throw new IllegalArgumentException();
	}
}
