package at.pcgamingfreaks.service.thirdparty.data;

import at.pcgamingfreaks.model.enums.MediaType;
import at.pcgamingfreaks.model.enums.MediaSource;
import at.pcgamingfreaks.model.db.User;
import at.pcgamingfreaks.model.dto.ListEntryDTO;

import java.util.List;

public interface DataService {
	MediaSource getService();

	MediaType getContentType();

	List<ListEntryDTO> fetch(String username);

	void pull(String username);

	void update(long id, float score, User user);

	void push(String username);
}
