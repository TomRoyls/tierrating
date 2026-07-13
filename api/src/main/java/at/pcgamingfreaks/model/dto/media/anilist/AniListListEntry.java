package at.pcgamingfreaks.model.dto.media.anilist;

import at.pcgamingfreaks.model.enums.MediaState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AniListListEntry {
	private float score;
	private MediaState status;
	private AniListMedia media;
}
