package at.pcgamingfreaks.service;

import at.pcgamingfreaks.model.db.SyncJob;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.exceptions.MediaSourceNotConnectedException;
import at.pcgamingfreaks.model.exceptions.MediaSyncAlreadyQueued;
import at.pcgamingfreaks.model.repo.SyncJobRepository;
import at.pcgamingfreaks.service.thirdparty.data.DataFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static at.pcgamingfreaks.model.enums.SyncStatus.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class MediaSyncManager {

	private final SyncJobRepository syncJobRepository;
	private final DataFactory dataFactory;
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	// TODO: restart pending and in_progress jobs

	@Transactional
	public Long enqueueSync(User user, MediaSource source, MediaType type) {
		Optional<SyncJob> runningJob = syncJobRepository.findActiveSyncByUserAndSourceAndTypeAndStatus(user, source, type, List.of(IN_PROGRESS, PENDING));
		if (runningJob.isPresent()) {
			log.debug("Tried to enqueue sync for {} {} {}, but sync already queued or in progress", user.getUsername(), source, type);
			// TODO: could i simply return the already existing id?
			throw new MediaSyncAlreadyQueued(user.getUsername(), source, type);
		}

		if (!user.getConnections().containsKey(source)) throw new MediaSourceNotConnectedException(user.getUsername(), source);

		SyncJob job = new SyncJob();
		job.setUser(user);
		job.setMediaSource(source);
		job.setMediaType(type);
		job.setStatus(PENDING);
		syncJobRepository.saveAndFlush(job);

		executor.submit(new MediaSyncJob(job, dataFactory.getProvider(source, type), syncJobRepository));
		log.debug("Submitted sync job (id: {}) for {} {} {}", job.getId(), user.getUsername(), source, type);

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
