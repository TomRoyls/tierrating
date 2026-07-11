package at.pcgamingfreaks.service.info;

import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.dto.ThirdPartyInfoResponseDTO;

public interface ThirdPartyInfoService {
	MediaSource getService();

	ThirdPartyInfoResponseDTO info();
}
