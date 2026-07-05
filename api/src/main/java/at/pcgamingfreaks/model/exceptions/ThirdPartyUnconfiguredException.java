package at.pcgamingfreaks.model.exceptions;

import at.pcgamingfreaks.model.enums.MediaSource;
import lombok.Getter;

public class ThirdPartyUnconfiguredException extends RuntimeException {

	@Getter
	private final MediaSource unconfiguredService;

	public ThirdPartyUnconfiguredException(MediaSource unconfiguredService) {
		this.unconfiguredService = unconfiguredService;
	}
}
