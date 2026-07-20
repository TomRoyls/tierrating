package at.pcgamingfreaks.model.exceptions;

import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class MediaSourceUnconfiguredException extends RuntimeException {

	@Getter
	private final MediaSource unconfiguredService;

	@Getter
	private MediaType unconfiguredType;
}
