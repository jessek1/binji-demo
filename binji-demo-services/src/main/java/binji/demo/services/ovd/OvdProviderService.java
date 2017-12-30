package binji.demo.services.ovd;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import binji.demo.common.dto.ovd.OvdProvider;

/**
 * Ovd provider service interface
 * 
 * @author jesse keane
 *
 */
public interface OvdProviderService {
	
	public static final String CACHE_NAME = "provider";
	
	OvdProvider getOvdProviderById(Long id);
	
	List<OvdProvider> getOvdProviderByProviderId(Integer providerId);
	
	OvdProvider getOvdProviderByProviderIdAndName(Integer providerId, String name);
	
	Page<OvdProvider> getOvdProviders(Pageable pageable);
	
	OvdProvider addOvdProvider(OvdProvider ovdProvider);
	
	OvdProvider updateOvdProvider(OvdProvider ovdProvider);
	
	void removeOvdProvider(OvdProvider ovdProvider);
}
