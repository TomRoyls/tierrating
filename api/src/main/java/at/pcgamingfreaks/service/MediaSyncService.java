package at.pcgamingfreaks.service;

import at.pcgamingfreaks.model.db.SyncJob;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.dto.sync.SyncStatusDTO;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MediaSyncService {
	private final UserRepository userRepository;
	private final MediaSyncManager syncManager;

	@Transactional
	public SyncStatusDTO status(String username, MediaSource source, MediaType type) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		Optional<SyncJob> job = syncManager.getStatus(user, source, type);
		return new SyncStatusDTO();
	}

	@Transactional
	public SyncStatusDTO status(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		user.getConnections(); // TODO: check for all configured sources
		return new SyncStatusDTO();
	}

	@Transactional
	public Long enqueue(String username, MediaSource source, MediaType type) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		return syncManager.enqueueSync(user, source, type);
	}
}
