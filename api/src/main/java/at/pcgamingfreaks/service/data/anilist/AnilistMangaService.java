package at.pcgamingfreaks.service.data.anilist;


import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.mapper.ListEntryDtoMapper;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.repo.AniListEntryRepository;
import at.pcgamingfreaks.model.repo.AniListEntryScoreRepository;
import at.pcgamingfreaks.model.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AnilistMangaService extends AnilistDataService {

	public AnilistMangaService(UserRepository userRepository, AniListEntryScoreRepository aniListEntryScoreRepository, AniListEntryRepository aniListEntryRepository, ThirdPartyConfig thirdPartyConfig, ListEntryDtoMapper listEntryDtoMapper) {
		super(userRepository, aniListEntryScoreRepository, aniListEntryRepository, thirdPartyConfig, listEntryDtoMapper);
	}

	@Override
	public MediaType getContentType() {
		return MediaType.MANGA;
	}
}
