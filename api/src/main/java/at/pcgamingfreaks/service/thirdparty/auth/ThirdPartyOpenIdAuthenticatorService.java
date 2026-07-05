package at.pcgamingfreaks.service.thirdparty.auth;

import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.dto.ThirdPartyOpenIdAuthRequestDTO;

public interface ThirdPartyOpenIdAuthenticatorService {
	MediaSource getService();

	void auth(String username, ThirdPartyOpenIdAuthRequestDTO request);
}
