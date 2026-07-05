package at.pcgamingfreaks.model.db;

import at.pcgamingfreaks.model.ContentType;
import at.pcgamingfreaks.model.SyncStatus;
import at.pcgamingfreaks.model.ThirdPartyService;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sync_jobs")
public class SyncJob {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sync_jobs_seq")
	@SequenceGenerator(name = "sync_jobs_seq", allocationSize = 50)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@NotNull
	@Column(nullable = false)
	private ThirdPartyService mediaSource;

	@NotNull
	@Column(nullable = false)
	private ContentType mediaType;

	@NotNull
	@Column(nullable = false)
	private SyncStatus status;

	@Nullable
	private String error;

	@Column(nullable = false)
	@CreationTimestamp
	private LocalDateTime startedAt;

	private LocalDateTime completedAt;
}
