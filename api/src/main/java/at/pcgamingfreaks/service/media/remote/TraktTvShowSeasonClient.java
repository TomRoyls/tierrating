package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.model.RemoteSyncResult;
import at.pcgamingfreaks.model.db.media.TraktMediaEntry;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.exceptions.MediaSourceFetchException;
import at.pcgamingfreaks.service.TmdbCoverFinder;
import com.uwetrottmann.trakt5.entities.RatedSeason;
import com.uwetrottmann.trakt5.entities.UserSlug;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.RatingsFilter;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

@Service
@ConditionalOnBean({OkHttpClient.class, TmdbCoverFinder.class})
public class TraktTvShowSeasonClient extends TraktMediaClient {

	public TraktTvShowSeasonClient(OkHttpClient sharedOkHttpClient, ThirdPartyConfig config, TmdbCoverFinder tmdbCoverFinder) {
		super(sharedOkHttpClient, config, tmdbCoverFinder);
	}

	@Override
	public MediaType getType() {
		return MediaType.TV_SHOW_SEASON;
	}

	@Override
	protected List<RemoteSyncResult<TraktMediaEntry>> fetchRated(String userId, String accessToken) {
		try {
			Response<List<RatedSeason>> response = userClient(accessToken)
					.users()
					.ratingsSeasons(UserSlug.fromUsername(userId), RatingsFilter.ALL, Extended.FULL)
					.execute();

			if (!response.isSuccessful()) {
				throw new MediaSourceFetchException("Failed to fetch rated tv show seasons from " + getSource());
			}

			return response.body().stream()
					.map(ratedSeason -> {
						TraktMediaEntry entry = new TraktMediaEntry();
						entry.setId(ratedSeason.season.ids.trakt.longValue());
						entry.setType(getType());
						entry.setSeason(ratedSeason.season.number);
						entry.setTitle(String.format("%s Season %d", ratedSeason.show.title, ratedSeason.season.number));
						entry.setCoverUrl(tmdbCoverFinder.findSeason(ratedSeason.show.ids.tmdb, ratedSeason.season.number));
						return new RemoteSyncResult<>(entry, ratedSeason.rating.value, null);
					})
					.toList();
		} catch (IOException e) {
			throw new MediaSourceFetchException("Failed to fetch rated tv show seasons from " + getSource(), e);
		}
	}

	@Override
	protected List<RemoteSyncResult<TraktMediaEntry>> fetchWatched(String userId, String accessToken) {
		// api does not return watched seasons
		return List.of();
	}
}