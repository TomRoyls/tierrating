package at.pcgamingfreaks.controller;

import at.pcgamingfreaks.model.ContentType;
import at.pcgamingfreaks.model.db.Tier;
import at.pcgamingfreaks.model.db.Tierlist;
import at.pcgamingfreaks.model.auth.ThirdPartyConnection;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.dto.TierDTO;
import at.pcgamingfreaks.model.exceptions.ThirdPartyUnconfiguredException;
import at.pcgamingfreaks.model.repo.TierListsRepository;
import at.pcgamingfreaks.model.repo.UserRepository;
import at.pcgamingfreaks.service.TiersService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Stream;

import static at.pcgamingfreaks.model.ThirdPartyService.ANILIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class TiersControllerTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private TierListsRepository tierListsRepository;


	@InjectMocks
	private TiersService tiersService;

	private static Stream<Arguments> expectedExceptions() {
		return Stream.of(
				Arguments.of(Optional.empty(), UsernameNotFoundException.class),
				Arguments.of(Optional.of(new User()), ThirdPartyUnconfiguredException.class)
		);
	}

	@ParameterizedTest
	@MethodSource("expectedExceptions")
	public void setTierListNotFound(Optional<User> user, Class<RuntimeException> expectedException) {
		user.ifPresent(u -> u.setConnections(new HashMap<>()));
		when(userRepository.findByUsername(any())).thenReturn(user);
		assertThrows(expectedException, () -> tiersService.updateTierlist("testing", ANILIST, ContentType.ANIME, new ArrayList<>()));
	}

	private static Stream<Arguments> settingTierlist() {
		return Stream.of(
				Arguments.of(
						List.of(),
						List.of(new TierDTO(UUID.randomUUID(), "#111111", "testing 1", 10, 10),
								new TierDTO(UUID.randomUUID(), "#222222", "testing 2", 8, 8)),
						List.of(new Tier("#111111", "testing 1", 10, 10),
								new Tier("#222222", "testing 2", 8, 8)),
						List.of()
				),
				Arguments.of(
						List.of(new Tier("#666666", "testing 6", 10, 10),
								new Tier("#777777", "testing 7", 7, 7),
								new Tier("#888888", "testing 8", 4, 4),
								new Tier("#999999", "testing 9", 1, 1)),
						List.of(new TierDTO(UUID.randomUUID(), "#111111", "testing 1", 10, 10),
								new TierDTO(UUID.randomUUID(), "#222222", "testing 2", 8, 8),
								new TierDTO(UUID.randomUUID(), "#333333", "testing 3", 6, 6),
								new TierDTO(UUID.randomUUID(), "#444444", "testing 4", 4, 4)),
						List.of(new Tier("#111111", "testing 1", 10, 10),
								new Tier("#222222", "testing 2", 8, 8),
								new Tier("#333333", "testing 3", 6, 6),
								new Tier("#444444", "testing 4", 4, 4)),
						List.of(new Tier("#666666", "testing 6", 10, 10),
								new Tier("#777777", "testing 7", 7, 7),
								new Tier("#888888", "testing 8", 4, 4),
								new Tier("#999999", "testing 9", 1, 1))
				),
				Arguments.of(
						List.of(new Tier("#666666", "testing 6", 10, 10),
								new Tier("#777777", "testing 7", 7, 7)),
						List.of(new TierDTO(UUID.fromString("da2e6e6e-9fc8-4201-bb99-2d0416c939d9"), "#666789", "testing 6789", 10, 10),
								new TierDTO(UUID.fromString("6bf9f692-07bb-485e-9d73-1ee6fe364fba"), "#888888", "testing 8", 8, 8)),
						List.of(new Tier("#666789", "testing 6789", 10, 10),
								new Tier("#888888", "testing 8", 8, 8)),
						List.of(new Tier("#777777", "testing 7", 7, 7))
				)
		);
	}

	@ParameterizedTest
	@MethodSource("settingTierlist")
	public void setTierListExisting(List<Tier> immutableExistingTiers, List<TierDTO> changedTiers, List<Tier> expectedTiers, List<Tier> expectedRemovedTiers) {
		User user = new User();
		user.setUsername("test");
		user.setConnections(Map.of(ANILIST, new ThirdPartyConnection()));
		when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

		List<Tier> existingTiers = new ArrayList<>(immutableExistingTiers);
		Tierlist tierList = new Tierlist();
		tierList.setUser(user);
		tierList.setTiers(existingTiers);
		when(tierListsRepository.findByUserAndServiceAndType(any(), any(), any())).thenReturn(!immutableExistingTiers.isEmpty() ? Optional.of(tierList) : Optional.empty());
		when(tierListsRepository.save(any())).thenReturn(null);

		ArgumentCaptor<Tierlist> tierListCaptor = ArgumentCaptor.forClass(Tierlist.class);
		ArgumentCaptor<List<Tier>> tiersCaptor = ArgumentCaptor.forClass(List.class);

		tiersService.updateTierlist("test", ANILIST, ContentType.ANIME, changedTiers);

		verify(tierListsRepository, times(1)).save(tierListCaptor.capture());

		if (!immutableExistingTiers.isEmpty()) { // without existing tierlist no deletion will occur
			List<Tier> removedTiers = tiersCaptor.getValue();
			assertEquals(expectedRemovedTiers.size(), removedTiers.size());
			for (int i = 0; i < removedTiers.size(); i++) {
				assertEquals(removedTiers.get(i).getColor(), expectedRemovedTiers.get(i).getColor());
				assertEquals(removedTiers.get(i).getName(), expectedRemovedTiers.get(i).getName());
				assertEquals(removedTiers.get(i).getScore(), expectedRemovedTiers.get(i).getScore());
				assertEquals(removedTiers.get(i).getAdjustedScore(), expectedRemovedTiers.get(i).getAdjustedScore());
			}
		}

		Tierlist capturedTierlist = tierListCaptor.getValue();
		assertEquals(user, capturedTierlist.getUser());
		assertEquals(expectedTiers.size(), capturedTierlist.getTiers().size());
		for (int i = 0; i < capturedTierlist.getTiers().size(); i++) {
			assertEquals(capturedTierlist.getTiers().get(i).getColor(), expectedTiers.get(i).getColor());
			assertEquals(capturedTierlist.getTiers().get(i).getName(), expectedTiers.get(i).getName());
			assertEquals(capturedTierlist.getTiers().get(i).getScore(), expectedTiers.get(i).getScore());
			assertEquals(capturedTierlist.getTiers().get(i).getAdjustedScore(), expectedTiers.get(i).getAdjustedScore());
		}
	}
}