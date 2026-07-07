package at.pcgamingfreaks.service;

import at.pcgamingfreaks.model.db.Tier;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultTierlistProvider {
	private static final List<Tier> GLOBAL_DEFAULTS = List.of(
			new Tier("S", "#FF7F7F", 10.0, 10.0),
			new Tier("A+", "#FF9E7F", 9.0, 9.0),
			new Tier("A", "#FFBF7F", 8.0, 8.0),
			new Tier("B+", "#E4E449", 7.0, 7.0),
			new Tier("B", "#AAE371", 6.0, 6.0),
			new Tier("C+", "#7FDFBF", 5.0, 5.0),
			new Tier("C", "#7FBFFF", 4.0, 4.0),
			new Tier("D", "#9F7FFF", 3.0, 3.0),
			new Tier("E", "#BF7FFF", 2.0, 2.0),
			new Tier("F", "#FF7FBF", 1.0, 1.0),
			new Tier("-", "#E6E6FF", 0.0, 0.0)
	);

	public List<Tier> getDefaultTierlist(MediaSource source, MediaType type) {
		return GLOBAL_DEFAULTS;
	}
}
