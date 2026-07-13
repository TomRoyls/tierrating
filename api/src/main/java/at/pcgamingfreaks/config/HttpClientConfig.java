package at.pcgamingfreaks.config;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {

	public static final String ANILIST_API_URL = "https://graphql.anilist.co";

	private final ThirdPartyConfig thirdPartyConfig;

	@Bean
	public WebClient anilistWebClient() {
		return WebClient.builder()
				.baseUrl(ANILIST_API_URL)
				// TODO: Set a global timeout so the client doesn't hang forever
				.build();
	}

	@Bean
	@ConditionalOnProperty(prefix = "thirdparty.anilist", name = {"key", "secret", "redirect-url"})
	public HttpGraphQlClient anilistGraphQlClient(WebClient anilistWebClient) {
		return HttpGraphQlClient.create(anilistWebClient);
	}

	@Bean
	@ConditionalOnProperty(prefix = "thirdparty.trakt", name = {"key", "secret", "redirect-url"})
	public OkHttpClient traktOkHttpClient() {
		return new OkHttpClient.Builder()
				.connectTimeout(20, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.build();
	}
}
