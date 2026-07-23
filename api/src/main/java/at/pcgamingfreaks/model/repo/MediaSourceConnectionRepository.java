package at.pcgamingfreaks.model.repo;

import at.pcgamingfreaks.model.db.MediaSourceConnection;
import at.pcgamingfreaks.model.enums.MediaSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaSourceConnectionRepository extends JpaRepository<MediaSourceConnection, Long> {

	Optional<MediaSourceConnection> findByUserIdAndSource(Long usedId, MediaSource source);

}
