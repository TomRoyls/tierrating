package at.pcgamingfreaks.service.data.trakt;

import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.mapper.ListEntryDtoMapper;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.exceptions.ThirdPartySyncException;
import at.pcgamingfreaks.model.repo.TraktEntryRepository;
import at.pcgamingfreaks.model.repo.TraktEntryScoreRepository;
import at.pcgamingfreaks.model.repo.UserRepository;
import at.pcgamingfreaks.model.db.media.TraktEntry;
import at.pcgamingfreaks.model.db.media.TraktEntryScore;
import at.pcgamingfreaks.service.TmdbCoverFinder;
import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.*;
import com.uwetrottmann.trakt5.enums.Extended;
import com.uwetrottmann.trakt5.enums.Rating;
import com.uwetrottmann.trakt5.enums.RatingsFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TraktMovieData extends TraktDataService {

	public TraktMovieData(UserRepository userRepository, TraktEntryScoreRepository entryScoreRepository, TraktEntryRepository entryRepository, TmdbCoverFinder coverFinder, ThirdPartyConfig thirdPartyConfig, ListEntryDtoMapper listEntryDtoMapper) {
		super(userRepository, entryScoreRepository, entryRepository, coverFinder, thirdPartyConfig, listEntryDtoMapper);
	}

	@Override
	public MediaType getContentType() {
		return MediaType.MOVIE;
	}

	@Override
	protected List<TraktEntryScore> pull(User user) {
		Map<Long, TraktEntryScore> entries = new HashMap<>();
		pullRated(user).stream()
				.map(ratedMovie -> {
					TraktEntry entry = new TraktEntry(
							ratedMovie.movie.ids.trakt,
							MediaType.MOVIE, null,
							ratedMovie.movie.title,
							coverFinder.findMovie(ratedMovie.movie.ids.tmdb)
					);
					TraktEntryScore entryScore = new TraktEntryScore();
					entryScore.setUser(user);
					entryScore.setEntry(entry);
					entryScore.setScore(ratedMovie.rating.value);
					return entryScore;
				})
				.forEach(entry -> entries.put(entry.getEntry().getId(), entry));
		pullWatched(user).stream()
				.map(baseMovie -> {
					TraktEntry entry = new TraktEntry(
							baseMovie.movie.ids.trakt,
							MediaType.MOVIE, null,
							baseMovie.movie.title,
							coverFinder.findMovie(baseMovie.movie.ids.tmdb)
					);
					TraktEntryScore entryScore = new TraktEntryScore();
					entryScore.setUser(user);
					entryScore.setEntry(entry);
					entryScore.setScore(0);
					return entryScore;
				})
				.forEach(entry -> entries.computeIfAbsent(entry.getEntry().getId(), key -> entry));
		return entries.values().stream().toList();
	}

	@Override
	protected List<RatedMovie> pullRated(User user) {
		try {
			Response<List<RatedMovie>> response = new TraktV2(
					thirdPartyConfig.getTrakt().getKey(),
					thirdPartyConfig.getTrakt().getSecret(),
					thirdPartyConfig.getTrakt().getRedirectUrl())
					.users()
					.ratingsMovies(
							UserSlug.fromUsername(user.getConnections().get(MediaSource.TRAKT).getThirdPartyUserId()),
							RatingsFilter.ALL,
							Extended.FULL)
					.execute();

			if (!response.isSuccessful())
				throw new ThirdPartySyncException("Error occurred fetching rated movies for " + user.getUsername());

			return response.body();
		} catch (IOException e) {
			throw new ThirdPartySyncException("Error occurred fetching rated movies: " + e.getMessage());
		}
	}

	@Override
	protected List<BaseMovie> pullWatched(User user) {
		try {
			Response<List<BaseMovie>> response = new TraktV2(
					thirdPartyConfig.getTrakt().getKey(),
					thirdPartyConfig.getTrakt().getSecret(),
					thirdPartyConfig.getTrakt().getRedirectUrl())
					.users()
					.watchedMovies(
							UserSlug.fromUsername(user.getConnections().get(MediaSource.TRAKT).getThirdPartyUserId()),
							Extended.FULL)
					.execute();

			if (!response.isSuccessful())
				throw new ThirdPartySyncException("Error occurred fetching watched movies of " + user.getUsername());

			return response.body();
		} catch (IOException e) {
			throw new ThirdPartySyncException("Error occurred fetching watched movies: " + e.getMessage());
		}
	}

	protected void pushSingleChange(long id, float score, User user) {
		try {
			new TraktV2(thirdPartyConfig.getTrakt().getKey(), thirdPartyConfig.getTrakt().getSecret(), thirdPartyConfig.getTrakt().getRedirectUrl())
					.accessToken(user.getConnections().get(MediaSource.TRAKT).getAccessToken())
					.sync()
					.addRatings(new SyncItems().movies(new SyncMovie()
							.id(MovieIds.trakt((int) id))
							.rating(Rating.fromValue((int) score))))
					.execute();
		} catch (IOException e) {
			log.error("Error pushing rating change to Trakt: {}", e.getMessage(), e);
			throw new ThirdPartySyncException("Failed to push rating change to Trakt: " + e.getMessage());
		}
	}
}
