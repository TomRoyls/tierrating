package at.pcgamingfreaks.service;

import at.pcgamingfreaks.model.db.SyncJob;
import at.pcgamingfreaks.model.enums.SyncStatus;
import at.pcgamingfreaks.model.repo.SyncJobRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class MediaSyncJob implements Runnable {
	private final SyncJob syncJob;

	private final SyncJobRepository syncJobRepository;

	@Override
	public void run() {
		syncJob.setStatus(SyncStatus.IN_PROGRESS);
		syncJobRepository.save(syncJob);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		syncJob.setCompletedAt(LocalDateTime.now());
		syncJob.setStatus(SyncStatus.COMPLETED);
		syncJobRepository.save(syncJob);
	}
}
