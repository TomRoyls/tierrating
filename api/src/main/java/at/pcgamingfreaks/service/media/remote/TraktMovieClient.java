package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.model.RemoteSyncResult;
import at.pcgamingfreaks.model.RemoteUpdateEntry;
import at.pcgamingfreaks.model.db.media.TraktMediaEntry;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.exceptions.MediaSourceFetchException;
import at.pcgamingfreaks.service.TmdbCoverFinder;
import com.uwetrottmann.trakt5.entities.*;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.Rating;
import com.uwetrottmann.trakt5.enums.RatingsFilter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@ConditionalOnBean({OkHttpClient.class, TmdbCoverFinder.class})
public class TraktMovieClient extends TraktMediaClient {

	public TraktMovieClient(OkHttpClient sharedOkHttpClient, ThirdPartyConfig config, TmdbCoverFinder tmdbCoverFinder) {
		super(sharedOkHttpClient, config, tmdbCoverFinder);
	}

	@Override
	public MediaType getType() {
		return MediaType.MOVIE;
	}

	@Override
	protected List<RemoteSyncResult<TraktMediaEntry>> fetchRated(String userId, String accessToken) {
		try {
			Response<List<RatedMovie>> response = userClient(accessToken)
					.users()
					.ratingsMovies(UserSlug.fromUsername(userId), RatingsFilter.ALL, Extended.FULL)
					.execute();
			if (!response.isSuccessful()) {
				throw new MediaSourceFetchException("Failed to fetch rated movies from " + getSource());
			}

			return response.body().stream()
					.map(ratedMovie -> new RemoteSyncResult<>(map(ratedMovie.movie), ratedMovie.rating.value, null))
					.toList();
		} catch (IOException e) {
			throw new MediaSourceFetchException("Failed to fetch rated movies from " + getSource(), e);
		}
	}

	@Override
	protected List<RemoteSyncResult<TraktMediaEntry>> fetchWatched(String userId, String accessToken) {
		try {
			Response<List<BaseMovie>> response = userClient(accessToken)
					.users()
					.watchedMovies(UserSlug.fromUsername(userId), Extended.FULL)
					.execute();
			if (!response.isSuccessful()) {
				throw new MediaSourceFetchException("Failed to fetch rated movies from " + getSource());
			}

			return response.body().stream()
					.map(baseMovie -> new RemoteSyncResult<>(map(baseMovie.movie), 0, null))
					.toList();
		} catch (IOException e) {
			throw new MediaSourceFetchException("Failed to fetch rated movies from " + getSource(), e);
		}
	}

	private TraktMediaEntry map(Movie movie) {
		TraktMediaEntry traktMediaEntry = new TraktMediaEntry();
		traktMediaEntry.setId(movie.ids.trakt.longValue());
		traktMediaEntry.setTitle(movie.title);
		traktMediaEntry.setType(getType());
		traktMediaEntry.setCoverUrl(tmdbCoverFinder.findMovie(movie.ids.tmdb));
		return traktMediaEntry;
	}

	@Override
	protected SyncItems createRemoteUpdate(List<RemoteUpdateEntry> updates) {
		List<SyncMovie> movies = new ArrayList<>();
		for (RemoteUpdateEntry entry : updates) {
			movies.add(new SyncMovie()
					.id(MovieIds.trakt((int) entry.id()))
					.rating(Rating.fromValue(toRemoteScore(entry.score())))
			);
		}
		return new SyncItems().movies(movies);
	}
}
