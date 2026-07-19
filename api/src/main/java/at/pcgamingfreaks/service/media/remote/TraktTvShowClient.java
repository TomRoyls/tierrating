package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.model.RemoteSyncResult;
import at.pcgamingfreaks.model.db.media.TraktMediaEntry;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.exceptions.MediaSourceFetchException;
import at.pcgamingfreaks.service.TmdbCoverFinder;
import com.uwetrottmann.trakt5.entities.BaseShow;
import com.uwetrottmann.trakt5.entities.RatedShow;
import com.uwetrottmann.trakt5.entities.Show;
import com.uwetrottmann.trakt5.entities.UserSlug;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.RatingsFilter;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

@Service
public class TraktTvShowClient extends TraktMediaClient {

	public TraktTvShowClient(OkHttpClient sharedOkHttpClient, ThirdPartyConfig config, TmdbCoverFinder tmdbCoverFinder) {
		super(sharedOkHttpClient, config, tmdbCoverFinder);
	}

	@Override
	public MediaType getType() {
		return MediaType.TV_SHOW;
	}

	@Override
	protected List<RemoteSyncResult<TraktMediaEntry>> fetchRated(String userId, String accessToken) {
		try {
			Response<List<RatedShow>> response = userClient(accessToken)
					.users()
					.ratingsShows(UserSlug.fromUsername(userId), RatingsFilter.ALL, Extended.NOSEASONS)
					.execute();

			if (!response.isSuccessful()) {
				throw new MediaSourceFetchException("Failed to fetch rated tv shows from " + getSource());
			}

			return response.body().stream()
					.map(ratedShow -> new RemoteSyncResult<>(map(ratedShow.show), ratedShow.rating.value, null))
					.toList();
		} catch (IOException e) {
			throw new MediaSourceFetchException("Failed to fetch rated tv shows from " + getSource(), e);
		}
	}

	@Override
	protected List<RemoteSyncResult<TraktMediaEntry>> fetchWatched(String userId, String accessToken) {
		try {
			Response<List<BaseShow>> response = userClient(accessToken)
					.users()
					.watchedShows(UserSlug.fromUsername(userId), Extended.NOSEASONS)
					.execute();

			if (!response.isSuccessful()) {
				throw new MediaSourceFetchException("Failed to fetch watched tv shows from " + getSource());
			}

			return response.body().stream()
					.map(baseShow -> new RemoteSyncResult<>(map(baseShow.show), 0, null))
					.toList();
		} catch (IOException e) {
			throw new MediaSourceFetchException("Failed to fetch watched tv shows from " + getSource(), e);
		}
	}

	private TraktMediaEntry map(Show show) {
		TraktMediaEntry traktMediaEntry = new TraktMediaEntry();
		traktMediaEntry.setId(show.ids.trakt.longValue());
		traktMediaEntry.setTitle(show.title);
		traktMediaEntry.setType(getType());
		traktMediaEntry.setSeason(null);
		traktMediaEntry.setCoverUrl(tmdbCoverFinder.findShow(show.ids.tmdb));
		return traktMediaEntry;
	}
}