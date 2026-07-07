package at.pcgamingfreaks.model.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Tier {

	@NotNull
	@Column(nullable = false)
	private String name;

	private String color;

	@NotNull
	@Column(nullable = false)
	private double score;

	@NotNull
	@Column(nullable = false)
	private double adjustedScore;

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Tier tier = (Tier) o;
		return Double.compare(score, tier.score) == 0
				&& Double.compare(adjustedScore, tier.adjustedScore) == 0
				&& color.equals(tier.color)
				&& name.equals(tier.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(color, name, score, adjustedScore);
	}
}
