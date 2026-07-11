package at.pcgamingfreaks.model.dto.media.anilist;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AniListPage {
	private AniListPageInfo pageInfo;
	private List<AniListListEntry> mediaList;
}
