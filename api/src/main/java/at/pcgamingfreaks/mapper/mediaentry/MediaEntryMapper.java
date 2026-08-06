package at.pcgamingfreaks.mapper.mediaentry;

import at.pcgamingfreaks.model.db.media.MediaEntry;
import at.pcgamingfreaks.model.db.media.UserMediaEntryState;
import at.pcgamingfreaks.model.dto.MediaEntryDTO;
import at.pcgamingfreaks.model.enums.MediaSource;

public interface MediaEntryMapper<E extends MediaEntry> {

	MediaSource getSource();

	// TODO: this can later be expanded with more preferences
	MediaEntryDTO toDTO(E entry, UserMediaEntryState userState);
}
