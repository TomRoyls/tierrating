package at.pcgamingfreaks.exceptions;

import at.pcgamingfreaks.model.enums.MediaType;

public class EntryNotFoundException extends RuntimeException {
	public EntryNotFoundException(String message) {
		super(message);
	}

	public EntryNotFoundException(MediaType type, long id) {
		super(String.format("%s entry with id %d not found", type, id));
	}
}
