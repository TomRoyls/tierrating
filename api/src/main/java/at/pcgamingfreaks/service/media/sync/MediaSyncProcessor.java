package at.pcgamingfreaks.service.media.sync;

import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.repo.MediaEntryRepository;
import at.pcgamingfreaks.model.repo.UserMediaEntryStateRepository;
import at.pcgamingfreaks.model.repo.UserRepository;
import at.pcgamingfreaks.service.media.remote.RemoteClientRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaSyncProcessor {

	private final RemoteClientRegistry remoteClientRegistry;
	private final MediaEntryRepository mediaEntryRepository;
	private final UserMediaEntryStateRepository userMediaEntryStateRepository;
	private final UserRepository userRepository;

	public void processSync(Long userId, MediaSource source, MediaType type) {

	}
}
