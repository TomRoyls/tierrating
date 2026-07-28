package at.pcgamingfreaks.integration;

import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.TraktV2Interceptor;
import okhttp3.OkHttpClient;

public class SharedTraktV2 extends TraktV2 {
	private final OkHttpClient sharedOkHttpClient;

	public SharedTraktV2(String apiKey, String clientSecret, String redirectUri, OkHttpClient sharedOkHttpClient) {
		super(apiKey, clientSecret, redirectUri);
		this.sharedOkHttpClient = sharedOkHttpClient;
	}

	@Override
	protected synchronized OkHttpClient okHttpClient() {
		return sharedOkHttpClient.newBuilder()
				.addInterceptor(new TraktV2Interceptor(this))
				.build();
	}
}
