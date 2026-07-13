package at.pcgamingfreaks.model.db.media;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "anilist_media_entries")
public class AniListMediaEntry extends MediaEntry {
	private String titleRomaji;
}
