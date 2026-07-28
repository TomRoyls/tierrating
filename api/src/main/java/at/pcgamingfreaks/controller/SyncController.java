package at.pcgamingfreaks.controller;

import at.pcgamingfreaks.model.UserPrincipal;
import at.pcgamingfreaks.model.dto.sync.SyncStatusDTO;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.service.sync.MediaSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("sync")
@RequiredArgsConstructor
public class SyncController {

	private final MediaSyncService mediaSyncService;

	@GetMapping("status/{source}/{type}")
	public ResponseEntity<SyncStatusDTO> status(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable MediaSource source, @PathVariable MediaType type) {
		return ResponseEntity.ok(mediaSyncService.status(userPrincipal.getUsername(), source, type));
	}

	@PostMapping("{source}/{type}")
	public ResponseEntity<?> enqueue(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable MediaSource source, @PathVariable MediaType type) {
		mediaSyncService.enqueue(userPrincipal, source, type);
		return ResponseEntity.status(202).build();
	}
}
