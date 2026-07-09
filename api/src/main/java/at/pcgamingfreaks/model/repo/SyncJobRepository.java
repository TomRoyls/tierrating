package at.pcgamingfreaks.model.repo;

import at.pcgamingfreaks.model.db.SyncJob;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SyncJobRepository extends JpaRepository<SyncJob, Long> {

	@Query("select job from SyncJob job where job.user = ?1 and job.mediaSource = ?2 and job.mediaType = ?2 and job.status in (IN_PROGRESS, PENDING)")
	Optional<SyncJob> findActiveSyncByUserAndSourceAndType(User user, MediaSource source, MediaType type);

	Optional<SyncJob> findByUserAndMediaSourceAndMediaTypeOrderByStartedAtDesc(User user, MediaSource source, MediaType type);
}

