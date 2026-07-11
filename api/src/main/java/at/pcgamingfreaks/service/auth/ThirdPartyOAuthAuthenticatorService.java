package at.pcgamingfreaks.service.auth;

import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.dto.ThirdPartyOAuthRequestDTO;

public interface ThirdPartyOAuthAuthenticatorService {
	MediaSource getMediaSource();

	void auth(String username, ThirdPartyOAuthRequestDTO request);
}
