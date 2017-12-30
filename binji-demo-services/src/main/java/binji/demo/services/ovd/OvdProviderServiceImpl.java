package binji.demo.services.ovd;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import binji.demo.common.dto.D360PageImpl;
import binji.demo.common.dto.ovd.OvdProvider;
import binji.demo.core.caching.CacheKeyUtils;
import binji.demo.core.caching.D360Cache;
import binji.demo.data.entities.ovd.OvdProviderEntity;
import binji.demo.data.entities.ovd.QOvdProviderEntity;
import binji.demo.data.repositories.ovd.OvdProviderRepository;
import binji.demo.mapper.ovd.OvdProviderMapper;


/**
 * @author jesse keane
 *
 */
public class OvdProviderServiceImpl implements OvdProviderService {
	
	private static final long DEFAULT_TTL = 1440 * 60;
	
	/**
	 * Key for caching 
	 *  0 - id
	*/	
	private static final String OVDPROVIDER_BY_ID_KEY = "ovdprovider.id-%s";
	private static final long OVDPROVIDER_BY_ID_TTL = DEFAULT_TTL;
	
	/**
	 * Key for caching 
	 *  0 - provider id
	*/	
	private static final String OVDPROVIDERS_BY_PROVIDERID_KEY = "ovdproviders.providerid-%s";
	private static final long OVDPROVIDERS_BY_PROVIDERID_TTL = DEFAULT_TTL;
	
	/**
	 * Key for caching 
	 *  0 - provider id
	 *  1 - name
	*/	
	private static final String OVDPROVIDERS_BY_PROVIDERID_AND_NAME_KEY = "ovdprovider.providerid.name-%s-%s";
	private static final long OVDPROVIDERS_BY_PROVIDERID_AND_NAME_TTL = DEFAULT_TTL;
	
	/**
	 * Key for caching 
	 *  0 - pageable
	*/	
	private static final String OVDPROVIDERS_ALL_KEY = "ovdprovider.all-%s";
	private static final long OVDPROVIDERS_ALL_TTL = DEFAULT_TTL;
	private static final String OVDPROVIDERS_ALL_SETKEY = "ovdprovider.all.set";
	
	private final OvdProviderRepository ovdProviderRepository;
	private final OvdProviderMapper ovdProviderMapper;
	private final D360Cache cache;
	
	@PersistenceContext
	EntityManager entityManager;
	private final QOvdProviderEntity qEntry = QOvdProviderEntity.ovdProviderEntity;
	
	public OvdProviderServiceImpl(final OvdProviderRepository ovdProviderRepository,
			final OvdProviderMapper ovdProviderMapper,
			final CacheManager cacheManager) {
		this.ovdProviderRepository = ovdProviderRepository;
		this.ovdProviderMapper = ovdProviderMapper;
		this.cache = (D360Cache)cacheManager.getCache(OvdProviderService.CACHE_NAME);
	}
	
	

	@Override
	public OvdProvider getOvdProviderById(Long id) {
		String key = String.format(OVDPROVIDER_BY_ID_KEY, id);
		
		return cache.get(key, OVDPROVIDER_BY_ID_TTL, () -> {
			OvdProviderEntity entity = ovdProviderRepository.findOne(id);	
			return entity != null ? ovdProviderMapper.mapToDto(entity) : null;	
		});
	}

	@Override
	public List<OvdProvider> getOvdProviderByProviderId(Integer providerId) {
		String key = String.format(OVDPROVIDERS_BY_PROVIDERID_KEY, providerId);
		
		return cache.get(key, OVDPROVIDERS_BY_PROVIDERID_TTL, () -> {
			List<OvdProviderEntity> entities = ovdProviderRepository.findByProviderId(providerId);	
			return entities != null ? ovdProviderMapper.mapToDtoList(entities) : null;	
		});
	}
	
	@Override
	public OvdProvider getOvdProviderByProviderIdAndName(Integer providerId, String name) {
		String key = String.format(OVDPROVIDERS_BY_PROVIDERID_AND_NAME_KEY, providerId, name);
		
		return cache.get(key, OVDPROVIDERS_BY_PROVIDERID_AND_NAME_TTL, () -> {
			OvdProviderEntity entity = ovdProviderRepository.findByProviderIdAndName(providerId, name);	
			return entity != null ? ovdProviderMapper.mapToDto(entity) : null;	
		});
	}
	
	@Override
	public Page<OvdProvider> getOvdProviders(Pageable pageable) {
		String key = String.format(OVDPROVIDERS_ALL_KEY, CacheKeyUtils.getKey(pageable));
		
		return cache.get(key, OVDPROVIDERS_ALL_SETKEY, OVDPROVIDERS_ALL_TTL, () -> {
			Page<OvdProviderEntity> page = ovdProviderRepository.findAll(pageable);	
			List<OvdProvider> dtoList = ovdProviderMapper.mapToDtoList(page.getContent());
			return new D360PageImpl<OvdProvider>(dtoList, pageable, page.getTotalElements());
		});
	}

	

	@Override
	public OvdProvider addOvdProvider(OvdProvider ovdProvider) {
		OvdProviderEntity entity = ovdProviderMapper.mapToEntity(ovdProvider);
		entity = ovdProviderRepository.saveAndFlush(entity);
		return ovdProviderMapper.mapToDto(entity);
	}

	@Override
	public OvdProvider updateOvdProvider(OvdProvider ovdProvider) {
		OvdProviderEntity entity = ovdProviderMapper.mapToEntity(ovdProvider);
		entity = ovdProviderRepository.saveAndFlush(entity);
		purgeCache(entity);
		return ovdProviderMapper.mapToDto(entity);
	}

	@Override
	public void removeOvdProvider(OvdProvider ovdProvider) {
		// TODO Auto-generated method stub
		
	}
	
	private void purgeCache(OvdProviderEntity entity) {
		cache.evict(String.format(OVDPROVIDER_BY_ID_KEY, entity.getId()));	
		cache.evict(String.format(OVDPROVIDERS_BY_PROVIDERID_KEY, entity.getProviderId()));
		cache.evict(String.format(OVDPROVIDERS_BY_PROVIDERID_AND_NAME_KEY, entity.getProviderId(), entity.getName()));
		cache.evictSet(OVDPROVIDERS_ALL_SETKEY);
	}



	

}
