package at.pcgamingfreaks.service.tokenfreshing;

import at.pcgamingfreaks.model.db.User;

public interface TokenRefresher {
	boolean isValid();

	void refresh(User user);
}
