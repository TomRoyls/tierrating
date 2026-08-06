package at.pcgamingfreaks.mapper.mediaentry;

import at.pcgamingfreaks.model.db.media.SteamMediaEntry;
import at.pcgamingfreaks.model.db.media.UserMediaEntryState;
import at.pcgamingfreaks.model.dto.MediaEntryDTO;
import at.pcgamingfreaks.model.enums.MediaSource;
import org.springframework.stereotype.Service;

@Service
public class SteamMediaEntryMapper extends BaseMediaEntryMapper<SteamMediaEntry> {

	@Override
	public MediaSource getSource() {
		return MediaSource.STEAM;
	}

	@Override
	public MediaEntryDTO toDTO(SteamMediaEntry entry, UserMediaEntryState userState) {
		return mapBase(entry, userState);
	}
}
