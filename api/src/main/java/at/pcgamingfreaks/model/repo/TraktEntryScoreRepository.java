package at.pcgamingfreaks.model.repo;

import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.thirdparty.trakt.TraktEntryScore;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

public interface TraktEntryScoreRepository extends CrudRepository<TraktEntryScore, UUID> {

	Set<TraktEntryScore> findAllByUserAndEntry_TypeOrderByScoreDesc(User user, MediaType type);

	Optional<TraktEntryScore> findByUserAndEntry_Id(User user, long id);

	List<TraktEntryScore> findAllByUserAndEntryIdIn(User user, Collection<Long> ids);

	void deleteAllByUser(User user);
}
