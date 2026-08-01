package at.pcgamingfreaks.model.dto;

import at.pcgamingfreaks.model.enums.MediaState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateScoreRequestDTO {
	private long id;
	private float score;
	private MediaState state;
}
