package at.pcgamingfreaks.model.repo;

import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.db.Tierlist;
import at.pcgamingfreaks.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TierListsRepository extends JpaRepository<Tierlist, Long> {

	Optional<Tierlist> findByUserAndServiceAndType(User user, MediaSource service, MediaType type);
}
