package at.pcgamingfreaks.exceptions;

public class ThirdPartyAuthenticationException extends RuntimeException {
	public ThirdPartyAuthenticationException(Throwable cause) {
		super(cause);
	}

	public ThirdPartyAuthenticationException(String message) {
		super(message);
	}
}
