package at.pcgamingfreaks.service.thirdparty.data;

import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.enums.MediaSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataFactory {
	private final Map<MediaSource, Map<MediaType, DataService>> providers;

	@Autowired
	public DataFactory(List<DataService> providerList) {
		Map<MediaSource, List<DataService>> providersByService = providerList.stream()
				.collect(Collectors.groupingBy(DataService::getService));
		providers = providersByService.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
						.collect(Collectors.toMap(DataService::getContentType, provider -> provider))));
	}

	public Map<MediaType, DataService> getProvider(MediaSource service) {
		Map<MediaType, DataService> dataServices = providers.get(service);
		if (dataServices == null) {
			throw new IllegalArgumentException("No provider found for service: " + service);
		}
		return dataServices;
	}

	public DataService getProvider(MediaSource service, MediaType mediaType) {
		DataService provider = providers.containsKey(service) ? providers.get(service).get(mediaType) : null;
		if (provider == null) {
			throw new IllegalArgumentException("No provider found for service: " + service);
		}
		return provider;
	}
}
