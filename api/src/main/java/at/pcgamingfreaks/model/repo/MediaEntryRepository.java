package at.pcgamingfreaks.model.repo;

import at.pcgamingfreaks.model.db.media.MediaEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaEntryRepository extends JpaRepository<Long, MediaEntry> {
}
