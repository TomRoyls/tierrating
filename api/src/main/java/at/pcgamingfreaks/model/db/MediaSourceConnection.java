package at.pcgamingfreaks.model.db;

import at.pcgamingfreaks.model.ContentType;
import at.pcgamingfreaks.model.ThirdPartyService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(
		name = "media_source_connections",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"third_party_user_id", "service"})
		}
)
public class MediaSourceConnection {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_source_connections_seq")
	@SequenceGenerator(name = "media_source_connections_seq", allocationSize = 50)
	private Long id;

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private User user;

	@NotBlank
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ThirdPartyService service;

	@NotBlank
	@Column(nullable = false)
	private String thirdPartyUserId;

	@NotBlank
	@Column(length = 2047, nullable = false)
	private String accessToken;

	@Column(length = 2047)
	private String refreshToken;

	private LocalDateTime expiresOn;

	@OneToMany(mappedBy = "connection", orphanRemoval = true)
	@MapKey(name = "type")
	Map<ContentType, MediaTypeSettings> mediaTypeSettings;

	public void putMediaTypeSettings(MediaTypeSettings settings) {
		if (settings.getType() == null) throw new IllegalStateException("Type is required for MediaTypeSettings");
		settings.setConnection(this);
		this.mediaTypeSettings.put(settings.getType(), settings);
	}
}
