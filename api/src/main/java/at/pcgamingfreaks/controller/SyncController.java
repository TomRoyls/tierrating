package at.pcgamingfreaks.controller;

import at.pcgamingfreaks.model.dto.sync.SyncStatusDTO;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.service.MediaSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("sync")
@RequiredArgsConstructor
public class SyncController {

	private final MediaSyncService mediaSyncService;

	@GetMapping("status/{username}/{source}/{type}")
	public ResponseEntity<SyncStatusDTO> status(@PathVariable String username, @PathVariable MediaSource source, @PathVariable MediaType type) {
		return ResponseEntity.ok(mediaSyncService.status(username, source, type));
	}

	@PostMapping("{username}/{source}/{type}")
	@PreAuthorize("authentication.principal.username == #username")
	public ResponseEntity<?> enqueue(@PathVariable String username, @PathVariable MediaSource source, @PathVariable MediaType type) {
		mediaSyncService.enqueue(username, source, type);
		return ResponseEntity.status(202).build();
	}
}
