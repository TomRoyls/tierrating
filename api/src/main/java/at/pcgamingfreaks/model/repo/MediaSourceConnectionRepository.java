package at.pcgamingfreaks.model.repo;

import at.pcgamingfreaks.model.db.MediaSourceConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaSourceConnectionRepository extends JpaRepository<MediaSourceConnection, Long> {

//	List<MediaSourceConnection> findAllByAutoImportSyncIsTrue();

}
