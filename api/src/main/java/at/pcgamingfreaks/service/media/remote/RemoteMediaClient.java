package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.model.RemoteSyncResult;
import at.pcgamingfreaks.model.db.MediaSourceConnection;
import at.pcgamingfreaks.model.db.media.MediaEntry;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;

import java.util.List;

public interface RemoteMediaClient<E extends MediaEntry> {

	MediaSource getSource();
	MediaType getType();

	List<RemoteSyncResult<E>> fetchRemote(MediaSourceConnection connection);

	default boolean shouldOverwriteLocal(float localScore, float remoteScore) {
		return localScore != remoteScore;
	}
}
