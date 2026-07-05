package at.pcgamingfreaks.model.enums;

import at.pcgamingfreaks.model.db.User;
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

	public static boolean hasUserConnection(User user, MediaSource service) {
		return user.getConnections().get(service) != null;
	}
}
