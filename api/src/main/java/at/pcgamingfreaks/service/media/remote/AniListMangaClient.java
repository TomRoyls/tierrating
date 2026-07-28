package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.model.enums.MediaType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(HttpGraphQlClient.class)
public class AniListMangaClient extends AniListMediaClient {

	public AniListMangaClient(HttpGraphQlClient anilistGraphQlClient) {
		super(anilistGraphQlClient);
	}

	@Override
	public MediaType getType() {
		return MediaType.MANGA;
	}
}
