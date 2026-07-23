package at.pcgamingfreaks.service.media.sync;

import at.pcgamingfreaks.model.RemoteSyncResult;
import at.pcgamingfreaks.model.db.MediaSourceConnection;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.db.media.MediaEntry;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.exceptions.MediaSourceNotConnectedException;
import at.pcgamingfreaks.model.repo.MediaSourceConnectionRepository;
import at.pcgamingfreaks.model.repo.UserRepository;
import at.pcgamingfreaks.service.media.remote.RemoteClientRegistry;
import at.pcgamingfreaks.service.media.remote.RemoteMediaClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaSyncProcessor {

	private final MediaSyncPersistenceService persistenceService;
	private final RemoteClientRegistry remoteClientRegistry;

	private final UserRepository userRepository;
	private final MediaSourceConnectionRepository mediaSourceConnectionRepository;


	public void processSync(Long userId, MediaSource source, MediaType type) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(userId.toString()));
		MediaSourceConnection connection = mediaSourceConnectionRepository.findByUserIdAndSource(userId, source)
				.orElseThrow(() -> new MediaSourceNotConnectedException(user.getUsername(), source));
		executeSync(userId, source, connection, remoteClientRegistry.getClient(source, type));
	}

	private <E extends MediaEntry> void executeSync(Long userId, MediaSource source,
	                                                MediaSourceConnection connection, RemoteMediaClient<E> client) {
		List<RemoteSyncResult<E>> remoteEntries = client.fetchRemote(connection);
		persistenceService.reconcile(userId, source, client, remoteEntries);
	}


}
