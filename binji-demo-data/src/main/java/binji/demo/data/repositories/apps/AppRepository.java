package binji.demo.data.repositories.apps;

import org.springframework.data.jpa.repository.JpaRepository;

import binji.demo.data.entities.apps.AppEntity;

/**
 * @author jesse keane
 *
 */
public interface AppRepository extends JpaRepository<AppEntity, Long> {
	AppEntity findByTitle(String title);
}