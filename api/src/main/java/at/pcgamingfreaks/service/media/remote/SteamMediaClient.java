package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.model.RemoteSyncResult;
import at.pcgamingfreaks.model.db.MediaSourceConnection;
import at.pcgamingfreaks.model.db.media.SteamMediaEntry;
import at.pcgamingfreaks.model.dto.media.steam.SteamOwnedGamesResponse;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaState;
import org.springframework.web.client.RestClient;

import java.util.List;

public abstract class SteamMediaClient implements RemoteMediaClient<SteamMediaEntry> {
	private static final String COVER_IMAGE_URL = "https://shared.fastly.steamstatic.com/store_item_assets/steam/apps/%s/library_600x900.jpg";

	private final RestClient steamRestClient;
	private final String apiKey;

	public SteamMediaClient(RestClient steamRestClient, ThirdPartyConfig config) {
		this.steamRestClient = steamRestClient;
		this.apiKey = config.getSteam().getKey();
	}

	@Override
	public MediaSource getSource() {
		return MediaSource.STEAM;
	}

	@Override
	public List<RemoteSyncResult<SteamMediaEntry>> fetchRemote(MediaSourceConnection connection) {
		SteamOwnedGamesResponse response = steamRestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/IPlayerService/GetOwnedGames/v0001/")
						.queryParam("key", apiKey)
						.queryParam("steamid", connection.getThirdPartyUserId())
						.queryParam("format", "json")
						.queryParam("include_appinfo", "true")
						.queryParam("include_played_free_games", "true")
						.build())
				.retrieve()
				.body(SteamOwnedGamesResponse.class);

		if (response == null || response.getResponse() == null || response.getResponse().getGames() == null) {
			return List.of();
		}

		return response.getResponse().getGames().stream()
				.map(gameInfo -> {
					SteamMediaEntry entry = new SteamMediaEntry();
					entry.setId(gameInfo.getAppId());
					entry.setTitle(gameInfo.getName());
					entry.setType(getType());
					entry.setCoverUrl(String.format(COVER_IMAGE_URL, gameInfo.getAppId()));
					return new RemoteSyncResult<>(entry, 0f, MediaState.OWNED);
				})
				.toList();
	}

	@Override
	public boolean shouldOverwriteLocal(float localScore, float remoteScore) {
		return false;
	}
}
