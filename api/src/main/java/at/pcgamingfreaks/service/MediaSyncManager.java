package at.pcgamingfreaks.service;

import at.pcgamingfreaks.model.db.SyncJob;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.enums.SyncStatus;
import at.pcgamingfreaks.model.repo.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static at.pcgamingfreaks.model.enums.SyncStatus.COMPLETED;


@Slf4j
@Component
@RequiredArgsConstructor
public class MediaSyncManager {

	private final SyncJobRepository syncJobRepository;
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	public Long enqueueSync(User user, MediaSource source, MediaType type) {
		Optional<SyncJob> runningJob = syncJobRepository.findActiveSyncByUserAndSourceAndType(user, source, type);
		if (runningJob.isPresent()) {
			log.debug("Tried to enqueue sync for {} {} {}, but sync already queued or in progress", user.getUsername(), source, type);
		}

		SyncJob job = new SyncJob();
		job.setUser(user);
		job.setMediaSource(source);
		job.setMediaType(type);
		job.setStatus(SyncStatus.PENDING);
		syncJobRepository.save(job);

		executor.submit(new MediaSyncJob(job, syncJobRepository));

		return job.getId();
	}

	public Optional<SyncJob> getStatus(User user, MediaSource source, MediaType type) {
		Optional<SyncJob> syncJob = syncJobRepository.findByUserAndMediaSourceAndMediaTypeOrderByStartedAtDesc(user, source, type);

		if (syncJob.isEmpty() || COMPLETED.equals(syncJob.get().getStatus())) {
			return Optional.empty();
		}

		return syncJob;
	}
}
