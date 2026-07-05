package at.pcgamingfreaks.model.repo;

import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.thirdparty.anilist.AniListEntryScore;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

public interface AniListEntryScoreRepository extends CrudRepository<AniListEntryScore, UUID> {

	Set<AniListEntryScore> findAllByUserAndEntry_TypeOrderByScoreDesc(User user, MediaType type);

	Optional<AniListEntryScore> findByUserAndEntry_Id(User user, long id);

	List<AniListEntryScore> findAllByUserAndEntryIdIn(User user, Collection<Long> ids);

	void deleteAllByUser(User user);
}
