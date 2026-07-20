package at.pcgamingfreaks.service.info;

import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.dto.ThirdPartyInfoResponseDTO;
import at.pcgamingfreaks.model.exceptions.MediaSourceUnconfiguredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AniListInfoService implements ThirdPartyInfoService {
	private final ThirdPartyConfig thirdPartyConfig;

	@Override
	public MediaSource getService() {
		return MediaSource.ANILIST;
	}

	public ThirdPartyInfoResponseDTO info() {
		if (!thirdPartyConfig.getAnilist().isValid()) throw new MediaSourceUnconfiguredException(getService());
		ThirdPartyInfoResponseDTO response = new ThirdPartyInfoResponseDTO();
		response.setClientId(thirdPartyConfig.getAnilist().getKey());
		return response;
	}
}
