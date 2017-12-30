package binji.demo.data.repositories.ovd;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import binji.demo.data.entities.ovd.OvdProviderEntity;

/**
 * @author jesse keane
 *
 */
public interface OvdProviderRepository extends JpaRepository<OvdProviderEntity, Long> {
	
	List<OvdProviderEntity> findByProviderId(Integer providerId);
	
	OvdProviderEntity findByProviderIdAndName(Integer providerId, String name);
}