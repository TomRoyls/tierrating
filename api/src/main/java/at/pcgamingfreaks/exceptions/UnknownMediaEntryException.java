package at.pcgamingfreaks.exceptions;

public class UnknownMediaEntryException extends RuntimeException {
	public UnknownMediaEntryException(Long id) {
		super(id.toString());
	}
}
