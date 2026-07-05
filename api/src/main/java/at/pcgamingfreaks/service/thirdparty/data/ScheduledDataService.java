package at.pcgamingfreaks.service.thirdparty.data;

import at.pcgamingfreaks.model.repo.MediaSourceConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledDataService {
	private final MediaSourceConnectionRepository mediaSourceConnectionRepository;
	private final DataFactory dataFactory;

//	@Scheduled(cron = "${sync.interval}")
//	public void pull() {
//		mediaSourceConnectionRepository.findAllByAutoImportSyncIsTrue().forEach(mediaSourceConnection -> {
//			dataFactory.getProvider(mediaSourceConnection.getSource()).values().forEach(service ->{
//				service.pull(mediaSourceConnection.getUser().getUsername());
//			});
//		});
//	}
}
