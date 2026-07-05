package at.pcgamingfreaks.model.db;

import at.pcgamingfreaks.model.ContentType;
import at.pcgamingfreaks.model.MediaState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "media_type_settings")
public class MediaTypeSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "connection_id")
	private MediaSourceConnection connection;

	private @NotNull ContentType type;

	private boolean loginPull = true;
	private boolean autoPush = true;

	private boolean showPublic = false;
	private boolean hidden = false;

	@ElementCollection
	@CollectionTable(
			name = "hidden_states",
			joinColumns = @JoinColumn(name = "media_type_settings_id")
	)
	@Enumerated(EnumType.STRING)
	@Column(name = "hidden_state")
	private List<MediaState> hiddenStates;
}
