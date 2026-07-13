package at.pcgamingfreaks.model;


import at.pcgamingfreaks.model.db.media.MediaEntry;
import at.pcgamingfreaks.model.enums.MediaState;

public record RemoteSyncResult<E extends MediaEntry>(
	E entry,
	float score,
	MediaState status
){}
