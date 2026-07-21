package at.pcgamingfreaks.model.repo;

import at.pcgamingfreaks.model.db.media.UserMediaEntryState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMediaEntryStateRepository extends JpaRepository<Long, UserMediaEntryState> {
}
