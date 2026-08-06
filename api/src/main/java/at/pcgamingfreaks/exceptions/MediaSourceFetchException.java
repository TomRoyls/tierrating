package at.pcgamingfreaks.exceptions;

public class MediaSourceFetchException extends RuntimeException {
	public MediaSourceFetchException(String message) {
		super(message);
	}

	public MediaSourceFetchException(String message, Throwable cause) {
		super(message, cause);
	}
}
