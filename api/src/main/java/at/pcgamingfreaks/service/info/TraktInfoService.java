package at.pcgamingfreaks.service.info;

import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.dto.ThirdPartyInfoResponseDTO;
import at.pcgamingfreaks.model.exceptions.ThirdPartyUnconfiguredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TraktInfoService implements ThirdPartyInfoService {
	private final ThirdPartyConfig thirdPartyConfig;

	@Override
	public MediaSource getService() {
		return MediaSource.TRAKT;
	}

	public ThirdPartyInfoResponseDTO info() {
		if (!thirdPartyConfig.getTrakt().isValid()) throw new ThirdPartyUnconfiguredException(getService());
		ThirdPartyInfoResponseDTO response = new ThirdPartyInfoResponseDTO();
		response.setClientId(thirdPartyConfig.getTrakt().getKey());
		return response;
	}
}
