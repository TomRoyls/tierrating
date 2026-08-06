package at.pcgamingfreaks.exceptions;

import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;

public class MediaSyncAlreadyQueued extends RuntimeException {
	public MediaSyncAlreadyQueued(String username, MediaSource source, MediaType type) {
		super(String.format("Sync for %s %s %s already queued", username, source, type));
	}
}
