package at.pcgamingfreaks.service.thirdparty.auth;

import at.pcgamingfreaks.config.ThirdPartyConfig;
import at.pcgamingfreaks.model.db.MediaSourceConnection;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.dto.ThirdPartyOAuthRequestDTO;
import at.pcgamingfreaks.model.exceptions.ThirdPartyAuthenticationException;
import at.pcgamingfreaks.model.exceptions.ThirdPartyUnconfiguredException;
import at.pcgamingfreaks.model.repo.MediaSourceConnectionRepository;
import at.pcgamingfreaks.model.repo.UserRepository;
import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.entities.AccessToken;
import com.uwetrottmann.trakt5.entities.UserSlug;
import com.uwetrottmann.trakt5.enums.Extended;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TraktAuthenticatorService implements ThirdPartyOAuthAuthenticatorService {
	private final UserRepository userRepository;
	private final MediaSourceConnectionRepository mediaSourceConnectionRepository;
	private final ThirdPartyConfig thirdPartyConfig;

	@Override
	public MediaSource getMediaSource() {
		return MediaSource.TRAKT;
	}

	@Override
	public void auth(String username, ThirdPartyOAuthRequestDTO request) {
		if (!thirdPartyConfig.getTrakt().isValid()) throw new ThirdPartyUnconfiguredException(getMediaSource());

		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		if (user.getConnections().get(getMediaSource()) != null)
			throw new ThirdPartyAuthenticationException("Already authenticated");

		try {
			TraktV2 trakt = new TraktV2(thirdPartyConfig.getTrakt().getKey(), thirdPartyConfig.getTrakt().getSecret(), thirdPartyConfig.getTrakt().getRedirectUrl());
			Response<AccessToken> response = trakt.exchangeCodeForAccessToken(request.getCode());
			if (!response.isSuccessful())
				throw new ThirdPartyAuthenticationException("Trakt OAuth responded with empty body");

			Response<com.uwetrottmann.trakt5.entities.User> traktUserInfo = trakt.accessToken(response.body().access_token).users().profile(UserSlug.ME, Extended.METADATA).execute();
			if (!traktUserInfo.isSuccessful())
				throw new ThirdPartyAuthenticationException("Trakt OAuth responded with empty username");

			MediaSourceConnection connection = new MediaSourceConnection();
			connection.setSource(getMediaSource());
			connection.setAccessToken(response.body().access_token);
			connection.setRefreshToken(response.body().refresh_token);
			connection.setExpiresOn(LocalDateTime.now().plusSeconds(response.body().expires_in));
			connection.setThirdPartyUserId(traktUserInfo.body().ids.slug);
			connection.setUser(user);
			mediaSourceConnectionRepository.save(connection);
		} catch (IOException e) {
			throw new ThirdPartyAuthenticationException(e);
		}
	}
}
