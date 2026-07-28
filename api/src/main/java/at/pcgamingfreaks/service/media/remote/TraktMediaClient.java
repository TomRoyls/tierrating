package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.integration.SharedTraktV2;
import at.pcgamingfreaks.model.RemoteSyncResult;
import at.pcgamingfreaks.model.db.MediaSourceConnection;
import at.pcgamingfreaks.model.db.media.TraktMediaEntry;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.service.TmdbCoverFinder;
import com.uwetrottmann.trakt5.TraktV2;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class TraktMediaClient implements RemoteMediaClient<TraktMediaEntry> {

	private final OkHttpClient sharedOkHttpClient;
	private final ThirdPartyConfig config;
	protected final TmdbCoverFinder tmdbCoverFinder;

	@Override
	public MediaSource getSource() {
		return MediaSource.TRAKT;
	}

	protected TraktV2 userClient(String accesstoken) {
		return new SharedTraktV2(
				config.getTrakt().getKey(),
				config.getTrakt().getSecret(),
				config.getTrakt().getRedirectUrl(),
				sharedOkHttpClient
		).accessToken(accesstoken);
	}

	public List<RemoteSyncResult<TraktMediaEntry>> fetchRemote(MediaSourceConnection connection) {
		Map<Long, RemoteSyncResult<TraktMediaEntry>> entries = new HashMap<>();
		fetchRated(connection.getThirdPartyUserId(), connection.getAccessToken())
				.forEach(entry -> entries.put(entry.entry().getId(), entry));
		fetchWatched(connection.getThirdPartyUserId(), connection.getAccessToken())
				.forEach(remoteEntry -> entries.computeIfAbsent(remoteEntry.entry().getId(), key -> remoteEntry));
		return entries.values().stream().toList();
	}

	protected abstract List<RemoteSyncResult<TraktMediaEntry>> fetchRated(String userId, String accessToken);

	protected abstract List<RemoteSyncResult<TraktMediaEntry>> fetchWatched(String userId, String accessToken);

	public boolean shouldOverwriteLocal(float localScore, float remoteScore) {
		return Math.round(localScore) != (int) remoteScore;
	}

}
