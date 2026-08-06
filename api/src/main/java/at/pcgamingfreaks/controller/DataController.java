package at.pcgamingfreaks.controller;

import at.pcgamingfreaks.model.RemoteUpdateEntry;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.dto.UpdateScoreRequestDTO;
import at.pcgamingfreaks.exceptions.MediaSourceUnconfiguredException;
import at.pcgamingfreaks.model.repo.UserRepository;
import at.pcgamingfreaks.service.media.remote.RemoteClientRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("data")
@RequiredArgsConstructor
public class DataController {
	private final UserRepository userRepository;
	private final RemoteClientRegistry remoteClientRegistry;

	/**
	 * Fetch data for username, service and type.
	 * If no data is synced yet and auto sync is active, pull data before returning result.
	 *
	 * @return mapped third-party data ordered by score descending
	 */
//	@GetMapping("fetch/{username}/{service}/{type}")
//	public ResponseEntity<List<ListEntryDTO>> fetch(@PathVariable String username,
//													@PathVariable MediaSource service,
//													@PathVariable MediaType type) {
//		DataService dataService = dataFactory.getProvider(service, type);
//		if (dataService == null) return ResponseEntity.notFound().build();
//		return ResponseEntity.ok(
//				dataService.fetch(username).stream()
//						.sorted(Comparator.comparing(ListEntryDTO::getScore).reversed())
//						.toList()
//		);
//	}

	/**
	 * Update score for resource.
	 * If auto sync is active, send changes directly to third-party service.
	 *
	 * @param request
	 */
	@PostMapping("update/{username}/{source}/{type}")
	@PreAuthorize("authentication.principal.username == #username")
	public void update(@PathVariable String username,
					   @PathVariable MediaSource source,
					   @PathVariable MediaType type,
					   @RequestBody UpdateScoreRequestDTO request) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		if (!user.hasMediaSourceConnection(source))
			throw new MediaSourceUnconfiguredException(source);
		remoteClientRegistry.getClient(source, type).pushRemote(user.getConnections().get(source), List.of(new RemoteUpdateEntry(request.getId(), request.getScore(), request.getState())));
	}

	/**
	 * Push score changes for user and type to third-party service.
	 *
	 * @param username
	 * @param service
	 * @param type
	 */
	@GetMapping("push/{username}/{service}/{type}")
	public void push(@PathVariable String username, @PathVariable MediaSource service, @PathVariable MediaType type) {

	}

}
