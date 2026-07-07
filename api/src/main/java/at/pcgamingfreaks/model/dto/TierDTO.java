package at.pcgamingfreaks.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TierDTO {
	private String name;
	private String color;
	private double score;
	private double adjustedScore;

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		TierDTO tier = (TierDTO) o;
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
