package at.pcgamingfreaks.model.db.media;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "trakt_media_entries")
public class TraktMediaEntry extends MediaEntry {
	@Nullable
	private Integer season;
}
