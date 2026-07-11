package at.pcgamingfreaks.model.dto.media.anilist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AniListPageInfo {
	private boolean hasNextPage;
	private int currentPage;
	private int perPage;
}
