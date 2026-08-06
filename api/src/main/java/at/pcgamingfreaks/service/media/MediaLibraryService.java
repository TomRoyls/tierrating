package at.pcgamingfreaks.service.media;

import at.pcgamingfreaks.mapper.mediaentry.MediaEntryMapper;
import at.pcgamingfreaks.mapper.mediaentry.MediaEntryMapperRegistry;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.db.media.MediaEntry;
import at.pcgamingfreaks.model.db.media.UserMediaEntryState;
import at.pcgamingfreaks.model.dto.MediaEntryDTO;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.repo.MediaEntryRepository;
import at.pcgamingfreaks.model.repo.UserMediaEntryStateRepository;
import at.pcgamingfreaks.model.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaLibraryService {

	private final UserRepository userRepository;
	private final UserMediaEntryStateRepository userMediaEntryStateRepository;
	private final MediaEntryRepositoryRegistry mediaEntryRepositoryRegistry;
	private final MediaEntryMapperRegistry mediaEntryMapperRegistry;

	public <E extends MediaEntry> List<MediaEntryDTO> fetchLocal(String username, MediaSource source, MediaType type) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

		// TODO: restrict lookup to specific states
		Map<Long, UserMediaEntryState> userStatesByMediaEntryId = userMediaEntryStateRepository.findAllByUserAndSource(user, source)
				.stream()
				.collect(Collectors.toMap(UserMediaEntryState::getEntryId, Function.identity()));

		MediaEntryRepository<E> mediaEntryRepository = mediaEntryRepositoryRegistry.getRepository(source);
		List<E> mediaEntries = mediaEntryRepository.findAllByIdInAndType(userStatesByMediaEntryId.keySet(), type);

		MediaEntryMapper<E> mapper = mediaEntryMapperRegistry.getMapper(source);
		return mediaEntries.stream()
				.map(entry -> mapper.toDTO(entry, userStatesByMediaEntryId.get(entry.getId())))
				.sorted(Comparator.comparing(MediaEntryDTO::getScore).reversed())
				.toList();
	}
}
