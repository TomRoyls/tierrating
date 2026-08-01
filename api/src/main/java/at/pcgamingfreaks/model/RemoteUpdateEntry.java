package at.pcgamingfreaks.model;

import at.pcgamingfreaks.model.enums.MediaState;

public record RemoteUpdateEntry(
		long id,
		float score,
		MediaState state
) {}
