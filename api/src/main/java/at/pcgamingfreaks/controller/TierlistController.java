package at.pcgamingfreaks.controller;

import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.dto.TierDTO;
import at.pcgamingfreaks.model.dto.TiersUpdateRequest;
import at.pcgamingfreaks.service.TierlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("tierlist")
@RequiredArgsConstructor
public class TierlistController {
	private final TierlistService tierlistService;

	/**
	 * @return list of tier grades sorted by score
	 */
	@GetMapping("{username}/{service}/{type}")
	public ResponseEntity<List<TierDTO>> getTierlist(@PathVariable String username, @PathVariable MediaSource service, @PathVariable MediaType type) {
		return ResponseEntity.ok(tierlistService.getTierlist(username, service, type));
	}

	@PutMapping("{username}/{service}/{type}")
	@PreAuthorize("authentication.principal.username == #username")
	public void setTierlist(@PathVariable String username, @PathVariable MediaSource service,
	                        @PathVariable MediaType type, @RequestBody TiersUpdateRequest request) {
		tierlistService.updateTierlist(username, service, type, request.getTiers());
	}
}
