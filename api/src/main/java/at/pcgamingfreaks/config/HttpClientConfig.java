package at.pcgamingfreaks.config;

import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientConfig {

	private static final String ANILIST_API_URL = "https://graphql.anilist.co";

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

	@Bean
	@ConditionalOnProperty(prefix = "thirdparty.steam", name = "api-key")
	public RestClient steamRestClient() {
		return RestClient.builder()
				.baseUrl("https://api.steampowered.com")
				.build();
	}
}
