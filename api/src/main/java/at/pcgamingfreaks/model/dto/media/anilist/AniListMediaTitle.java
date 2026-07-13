package at.pcgamingfreaks.model.dto.media.anilist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AniListMediaTitle {

	private String romaji;

	private String english;

	@JsonProperty("native")
	private String japanese;
}
