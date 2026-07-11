package at.pcgamingfreaks.model.dto.sync;

import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.enums.SyncStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyncStatusDTO {
	private MediaSource source;
	private MediaType type;
	private SyncStatus status;
	private LocalDateTime startedAt;
}
