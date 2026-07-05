package at.pcgamingfreaks.model.db;

import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.enums.MediaSource;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tierlists")
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id", "service", "type"})
})
public class Tierlist {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tierlists_seq")
	@SequenceGenerator(name = "tierlists_seq", allocationSize = 50)
	private Long id;

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User user;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MediaSource service;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MediaType type;

	@ElementCollection
	@CollectionTable(
			name = "tiers",
			joinColumns = @JoinColumn(name = "tierlist_id"),
			uniqueConstraints = {@UniqueConstraint(columnNames = {"tierlist_id", "score"})}
	)
	@OrderBy("score DESC")
	private List<Tier> tiers = new ArrayList<>();

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
