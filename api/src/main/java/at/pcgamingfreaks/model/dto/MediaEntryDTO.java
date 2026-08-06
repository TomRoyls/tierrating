package at.pcgamingfreaks.model.dto;

import at.pcgamingfreaks.model.enums.MediaState;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class MediaEntryDTO {
	private long id;
	private String title;
	private String cover;

	private float score;
	private MediaState state;

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		MediaEntryDTO that = (MediaEntryDTO) o;
		return id == that.id && Float.compare(score, that.score) == 0 && Objects.equals(title, that.title) && Objects.equals(cover, that.cover);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, cover, score);
	}
}
