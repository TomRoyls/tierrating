package at.pcgamingfreaks.service.media.remote;

import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.exceptions.MediaSourceUnconfiguredException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RemoteClientRegistry {
	private final Map<MediaSource, Map<MediaType, RemoteMediaClient<?>>> providers;

	public RemoteClientRegistry(List<RemoteMediaClient<?>> remoteClientList) {
		this.providers = remoteClientList.stream()
				.collect(Collectors.groupingBy(RemoteMediaClient::getSource, Collectors.toMap(
						RemoteMediaClient::getType,
						client -> client
				)));
	}

	public RemoteMediaClient<?> getClient(MediaSource source, MediaType type) {
		Map<MediaType, RemoteMediaClient<?>> typeMap = providers.get(source);

		if (typeMap == null) {
			throw new MediaSourceUnconfiguredException(source);
		}

		RemoteMediaClient<?> client = typeMap.get(type);
		if (client == null) {
			throw new MediaSourceUnconfiguredException(source, type);
		}

		return client;
	}
}
