package at.pcgamingfreaks.mapper.mediaentry;

import at.pcgamingfreaks.model.db.media.TraktMediaEntry;
import at.pcgamingfreaks.model.db.media.UserMediaEntryState;
import at.pcgamingfreaks.model.dto.MediaEntryDTO;
import at.pcgamingfreaks.model.enums.MediaSource;
import org.springframework.stereotype.Service;

import static at.pcgamingfreaks.model.enums.MediaType.TV_SHOW_SEASON;

@Service
public class TraktMediaEntryMapper extends BaseMediaEntryMapper<TraktMediaEntry> {

	@Override
	public MediaSource getSource() {
		return MediaSource.TRAKT;
	}

	@Override
	public MediaEntryDTO toDTO(TraktMediaEntry entry, UserMediaEntryState userState) {
		MediaEntryDTO dto = mapBase(entry, userState);
		if (TV_SHOW_SEASON.equals(entry.getType())) {
			dto.setTitle(String.format("%s Season %s", entry.getTitle(), entry.getSeason()));
		}
		return dto;
	}
}
