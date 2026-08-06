package at.pcgamingfreaks.mapper.mediaentry;

import at.pcgamingfreaks.model.db.media.AniListMediaEntry;
import at.pcgamingfreaks.model.db.media.UserMediaEntryState;
import at.pcgamingfreaks.model.dto.MediaEntryDTO;
import at.pcgamingfreaks.model.enums.MediaSource;
import org.springframework.stereotype.Service;

@Service
public class AniListMediaEntryMapper extends BaseMediaEntryMapper<AniListMediaEntry> {

	@Override
	public MediaSource getSource() {
		return MediaSource.ANILIST;
	}

	@Override
	public MediaEntryDTO toDTO(AniListMediaEntry entry, UserMediaEntryState userState) {
		MediaEntryDTO dto = mapBase(entry, userState);
		dto.setTitle(entry.getTitle() != null ? entry.getTitle() : entry.getTitleRomaji());
		return dto;
	}
}
