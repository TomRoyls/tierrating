package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.model.enums.MediaType;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

@Component
public class AniListAnimeClient extends AniListMediaClient {

	public AniListAnimeClient(HttpGraphQlClient anilistGraphQlClient) {
		super(anilistGraphQlClient);
	}

	@Override
	public MediaType getType() {
		return MediaType.ANIME;
	}
}
