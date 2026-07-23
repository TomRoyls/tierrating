package at.pcgamingfreaks.model.repo;

import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.db.media.UserMediaEntryState;
import at.pcgamingfreaks.model.enums.MediaSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMediaEntryStateRepository extends JpaRepository<UserMediaEntryState, Long> {
	List<UserMediaEntryState> findAllByUserAndSource(User user, MediaSource source);
}
