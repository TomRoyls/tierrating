package at.pcgamingfreaks.mapper.mediaentry;

import at.pcgamingfreaks.model.db.media.MediaEntry;
import at.pcgamingfreaks.model.db.media.UserMediaEntryState;
import at.pcgamingfreaks.model.dto.MediaEntryDTO;

public abstract class BaseMediaEntryMapper<E extends MediaEntry> implements MediaEntryMapper<E> {
	protected MediaEntryDTO mapBase(E entry, UserMediaEntryState userState) {
		MediaEntryDTO dto = new MediaEntryDTO();
		dto.setId(entry.getId());
		dto.setTitle(entry.getTitle());
		dto.setCover(entry.getCoverUrl());
		dto.setState(userState.getState());
		dto.setScore(userState.getScore());
		return dto;
	}
}
