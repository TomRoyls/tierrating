package at.pcgamingfreaks.model.db.media;

import at.pcgamingfreaks.model.enums.MediaType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class MediaEntry implements Persistable<Long> {
	@Id
	private Long id;

	@Enumerated(EnumType.STRING)
	private MediaType type;

	private String title;

	private String coverUrl;
	private String coverPath;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@Override
	public boolean isNew() {
		return createdAt == null;
	}
}
