package at.pcgamingfreaks.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MediaState {
	COMPLETED, IN_PROGRESS, PLANNING, DROPPED, PAUSED, OWNED;

	@JsonCreator
	public static MediaState fromString(String key) {
		if (key == null) {
			return null;
		}

		return switch (key) {
			case "CURRENT", "REPEATING" -> IN_PROGRESS;
			default -> MediaState.valueOf(key.toUpperCase());
		};
	}
}
