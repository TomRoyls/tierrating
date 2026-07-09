package at.pcgamingfreaks.controller;

import at.pcgamingfreaks.model.dto.sync.SyncStatusDTO;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
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

	@GetMapping("status/{username}/{source}/{type}")
	public ResponseEntity<SyncStatusDTO> status(@PathVariable String username, @PathVariable MediaSource source, @PathVariable MediaType type) {
		return ResponseEntity.ok(new SyncStatusDTO());
	}

	@GetMapping("status/{username}")
	public ResponseEntity<SyncStatusDTO> status(@PathVariable String username) {
		return ResponseEntity.ok(new SyncStatusDTO());
	}

	@PostMapping("{username}/{source}/{type}")
	@PreAuthorize("authentication.principal.username == #username")
	public ResponseEntity<?> enqueue(@PathVariable String username, @PathVariable MediaSource source, @PathVariable MediaType type) {
		return ResponseEntity.status(202).build();
	}
}
