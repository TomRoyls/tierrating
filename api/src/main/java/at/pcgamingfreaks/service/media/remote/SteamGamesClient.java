package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.model.enums.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SteamGamesClient extends SteamMediaClient {

	public SteamGamesClient(RestClient steamRestClient, ThirdPartyConfig config) {
		super(steamRestClient, config);
	}

	@Override
	public MediaType getType() {
		return MediaType.GAME;
	}
}
