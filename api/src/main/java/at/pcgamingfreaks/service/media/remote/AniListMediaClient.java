package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.model.RemoteSyncResult;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.db.media.AniListMediaEntry;
import at.pcgamingfreaks.model.dto.media.anilist.AniListListEntry;
import at.pcgamingfreaks.model.dto.media.anilist.AniListPage;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaState;
import at.pcgamingfreaks.model.enums.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.graphql.client.HttpGraphQlClient;

import java.util.ArrayList;
import java.util.List;

@ConditionalOnBean(HttpGraphQlClient.class)
@RequiredArgsConstructor
public abstract class AniListMediaClient implements RemoteMediaClient<AniListMediaEntry> {

	private final HttpGraphQlClient anilistGraphQlClient;

	private static final String query = """
					query ($userId: Int, $type: MediaType, $status: MediaListStatus, $page: Int, $perPage: Int) {
					    Page(page: $page, perPage: $perPage) {
					        pageInfo {
					            currentPage
					            hasNextPage
					            perPage
					        }
					        mediaList(userId: $userId, type: $type, status: $status) {
					            score(format: POINT_10_DECIMAL)
					            status
					            media {
					                id
					                title {
					                    romaji
					                    english
					                    native
					                }
					                coverImage {
					                    large
					                    extraLarge
					                }
					            }
					        }
					    }
					}
					""";

	@Override
	public MediaSource getSource() {
		return MediaSource.ANILIST;
	}

	private List<AniListListEntry> fetchFromApi(User user) {
		List<AniListListEntry> anilistQueryResult = new ArrayList<>();

		AniListPage page;
		int currentPage = 1;

		do {
			page = anilistGraphQlClient
					.mutate()
					.header("Authorization", "Bearer " + user.getConnections().get(getSource()).getAccessToken())
					.build()
					.document(query)
					.variable("userId", user.getConnections().get(getSource()).getThirdPartyUserId())
					.variable("type", getType().name())
					.variable("page", currentPage++)
					.variable("perPage", 50)
					.retrieveSync("Page")
					.toEntity(AniListPage.class);
			anilistQueryResult.addAll(page.getMediaList());
		} while (page.getPageInfo().isHasNextPage());

		return anilistQueryResult;
	}

	@Override
	public List<RemoteSyncResult<AniListMediaEntry>> fetchRemote(User user) {
		List<AniListListEntry> apiData = fetchFromApi(user);

		return apiData.stream().map(item -> {
			AniListMediaEntry entry = new AniListMediaEntry();
			entry.setId(item.getMedia().getId());
			entry.setTitle(item.getMedia().getTitle().getEnglish());
			entry.setTitleRomaji(item.getMedia().getTitle().getRomaji());
			entry.setType(MediaType.ANIME);
			entry.setCoverUrl(item.getMedia().getCoverImage().getExtraLarge());

			float score = item.getScore();
			MediaState status = item.getStatus();

			return new RemoteSyncResult<>(entry, score, status);
		}).toList();
	}
}
