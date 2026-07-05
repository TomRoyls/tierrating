package at.pcgamingfreaks.mapper;

import at.pcgamingfreaks.model.db.Tier;
import at.pcgamingfreaks.model.dto.TierDTO;

public class TierDtoMapper {
	public static TierDTO map(Tier tier) {
		TierDTO dto = new TierDTO();
		dto.setName(tier.getName());
		dto.setColor(tier.getColor());
		dto.setScore(tier.getScore());
		dto.setAdjustedScore(tier.getAdjustedScore());
		return dto;
	}

	public static Tier map(TierDTO dto) {
		Tier tier = new Tier();
		tier.setName(dto.getName());
		tier.setColor(dto.getColor());
		tier.setScore(dto.getScore());
		tier.setAdjustedScore(dto.getAdjustedScore());
		return tier;
	}
}
