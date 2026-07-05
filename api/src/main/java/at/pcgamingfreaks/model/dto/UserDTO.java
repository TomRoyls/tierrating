package at.pcgamingfreaks.model.dto;

import at.pcgamingfreaks.model.enums.MediaSource;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
	private Long id;

	private String username;
	private String bio;

	private Set<MediaSource> connectedServices;
}