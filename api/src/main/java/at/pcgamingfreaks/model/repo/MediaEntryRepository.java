package at.pcgamingfreaks.model.repo;

import at.pcgamingfreaks.model.db.media.MediaEntry;
import at.pcgamingfreaks.model.enums.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MediaEntryRepository<E extends MediaEntry> extends JpaRepository<E, Long> {
	List<E> findAllByIdIn(Collection<Long> ids);
	List<E> findAllByIdInAndType(Collection<Long> ids, MediaType type);
}
