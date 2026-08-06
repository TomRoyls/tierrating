package at.pcgamingfreaks.exceptions;

import at.pcgamingfreaks.model.enums.MediaSource;

public class MediaSourceNotConnectedException extends RuntimeException {
	public MediaSourceNotConnectedException(String username, MediaSource source) {
		super(String.format("%s has no %s connection", username, source));
	}
}
