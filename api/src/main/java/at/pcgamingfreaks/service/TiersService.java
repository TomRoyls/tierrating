package at.pcgamingfreaks.service;

import at.pcgamingfreaks.mapper.TierDtoMapper;
import at.pcgamingfreaks.model.ContentType;
import at.pcgamingfreaks.model.ThirdPartyService;
import at.pcgamingfreaks.model.db.Tierlist;
import at.pcgamingfreaks.model.auth.User;
import at.pcgamingfreaks.model.dto.TierDTO;
import at.pcgamingfreaks.model.exceptions.ThirdPartyUnconfiguredException;
import at.pcgamingfreaks.model.repo.TierListsRepository;
import at.pcgamingfreaks.model.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static at.pcgamingfreaks.model.ThirdPartyService.hasUserConnection;

@Service
@RequiredArgsConstructor
public class TiersService {
	private final UserRepository userRepository;
	private final TierListsRepository tierListsRepository;

	public List<TierDTO> getTierlist(String username, ThirdPartyService service, ContentType type) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		if (!hasUserConnection(user, service)) throw new ThirdPartyUnconfiguredException(service);

		Optional<Tierlist> tierlist = tierListsRepository.findByUserAndServiceAndType(user, service, type);

		List<TierDTO> tiers = tierlist.map(tierList -> tierList.getTiers().stream()
				.map(TierDtoMapper::map)
				.sorted(Comparator.comparing(TierDTO::getScore).reversed())
				.collect(Collectors.toList())
		).orElseGet(ArrayList::new);

		return tiers;
	}

	@Transactional
	public void updateTierlist(String username, ThirdPartyService service, ContentType type, List<TierDTO> changedTierlist) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		if (!hasUserConnection(user, service)) throw new ThirdPartyUnconfiguredException(service);

		Tierlist tierlist = tierListsRepository.findByUserAndServiceAndType(user, service, type).orElseGet(Tierlist::new);
		tierlist.setUser(user);
		tierlist.setService(service);
		tierlist.setType(type);

		tierlist.getTiers().clear();
		tierlist.getTiers().addAll(changedTierlist.stream()
				.map(TierDtoMapper::map)
				.toList());

		tierListsRepository.save(tierlist);
	}
}
