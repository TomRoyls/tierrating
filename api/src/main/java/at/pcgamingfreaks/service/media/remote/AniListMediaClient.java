package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.model.RemoteSyncResult;
import at.pcgamingfreaks.model.RemoteUpdateEntry;
import at.pcgamingfreaks.model.db.MediaSourceConnection;
import at.pcgamingfreaks.model.db.media.AniListMediaEntry;
import at.pcgamingfreaks.model.dto.media.anilist.AniListListEntry;
import at.pcgamingfreaks.model.dto.media.anilist.AniListPage;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaState;
import at.pcgamingfreaks.model.enums.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.client.HttpGraphQlClient;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class AniListMediaClient implements RemoteMediaClient<AniListMediaEntry> {

	private final HttpGraphQlClient anilistGraphQlClient;

	private static final String PULL_QUERY = """
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

	private static final String UPDATE_QUERY = """
			mutation ($listEntryId: Int, $mediaId: Int, $score: Float, $status: MediaListStatus) {
				SaveMediaListEntry(id: $listEntryId, mediaId: $mediaId, score: $score, status: $status) {
					id
					mediaId
					score,
					status
				}
			}
			""";

	@Override
	public MediaSource getSource() {
		return MediaSource.ANILIST;
	}

	private List<AniListListEntry> fetchFromApi(MediaSourceConnection connection) {
		List<AniListListEntry> anilistQueryResult = new ArrayList<>();

		AniListPage page;
		int currentPage = 1;

		do {
			page = anilistGraphQlClient
					.mutate()
					.header("Authorization", "Bearer " + connection.getAccessToken())
					.build()
					.document(PULL_QUERY)
					.variable("userId", connection.getThirdPartyUserId())
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
	public List<RemoteSyncResult<AniListMediaEntry>> fetchRemote(MediaSourceConnection connection) {
		List<AniListListEntry> apiData = fetchFromApi(connection);

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

	@Override
	public void pushRemote(MediaSourceConnection connection, List<RemoteUpdateEntry> updates) {
		for (RemoteUpdateEntry entry : updates) {
			anilistGraphQlClient
					.mutate()
					.header("Authorization", "Bearer " + connection.getAccessToken())
					.build()
					.document(UPDATE_QUERY)
					.variable("mediaId", entry.id())
					.variable("score", entry.score())
					.variable("status", toRemoteStatus(entry.state()))
					.retrieveSync("data.SaveMediaListEntry");
		}
	}

	protected String toRemoteStatus(MediaState state) {
		if (state == null) return null;

		return switch (state) {
			case IN_PROGRESS -> "CURRENT";
			case PLANNING -> "PLANNING";
			case COMPLETED -> "COMPLETED";
			case DROPPED -> "DROPPED";
			case PAUSED -> "PAUSED";
			default -> null;
		};
	}
}
