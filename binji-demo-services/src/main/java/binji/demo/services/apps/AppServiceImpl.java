package binji.demo.services.apps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.QueryModifiers;
import com.querydsl.jpa.impl.JPAQuery;

import binji.demo.common.dto.D360PageImpl;
import binji.demo.common.dto.apps.App;
import binji.demo.common.dto.ovd.OvdContentType;
import binji.demo.core.caching.CacheKeyUtils;
import binji.demo.core.caching.D360Cache;
import binji.demo.data.entities.apps.AppEntity;
import binji.demo.data.entities.apps.QAppEntity;
import binji.demo.data.repositories.apps.AppRepository;
import binji.demo.mapper.apps.AppMapper;

/**
 * @author jesse keane
 *
 */
public class AppServiceImpl implements AppService {
	
	private static final long DEFAULT_TTL = 1440 * 60;
	
	/**
	 * Key for caching 
	 *  0 - id
	*/	
	private static final String APP_BY_ID_KEY = "app.id-%s";
	private static final long APP_BY_ID_TTL = DEFAULT_TTL;
	
	/**
	 * Key for caching 
	 *  0 - title
	 *  1 - description
	 *  2 - genres
	 *  3 - pageable
	*/	
	private static final String APPS_FIND_KEY = "apps.find-%s-%s-%s-%s";
	private static final long APPS_FIND_TTL = DEFAULT_TTL;
	
	private final AppRepository appRepository;
	private final AppMapper appMapper;
	private final D360Cache cache;
	
	@PersistenceContext
	EntityManager entityManager;
	private final QAppEntity qEntry = QAppEntity.appEntity;
	
	public AppServiceImpl(final AppRepository appRepository,
			final AppMapper appMapper,
			final CacheManager cacheManager) {
		this.appRepository = appRepository;
		this.appMapper = appMapper;
		this.cache = (D360Cache)cacheManager.getCache(AppService.CACHE_NAME);
	}
	
	

	@Transactional
	@Override
	public App getAppById(Long id) {
		String key = String.format(APP_BY_ID_KEY, id);
		
		return cache.get(key, APP_BY_ID_TTL, () -> {
			AppEntity entity = appRepository.findOne(id);	
			return entity != null ? appMapper.mapToDto(entity) : null;	
		});
	}
	
	@Override
	public App getAppByTitle(String title) {		
		AppEntity entity = appRepository.findByTitle(title);
		return entity != null ? appMapper.mapToDto(entity) : null;			
	}

	
	@Transactional
	@Override
	public Page<App> findApps(String title, String description, String genres, Pageable pageable) {
		String key = String.format(APPS_FIND_KEY, title, description, genres, CacheKeyUtils.getKey(pageable));
		
		return cache.get(key, APPS_FIND_TTL, () -> {
			JPAQuery<AppEntity> query = new JPAQuery<AppEntity>(this.entityManager);
			query = query.from(qEntry);
					
			if (title != null && !title.isEmpty())
				query = query.where(qEntry.title.contains(title));
			if (description != null && !description.isEmpty())
				query = query.where(qEntry.description.contains(description));		
			if (genres != null && !genres.isEmpty()) {
				String genresArr[] = genres.split("\\s*,\\s*");
				List<String> genresTrimmed = new ArrayList<String>();
				for (int x=0; x < genresArr.length; x++) {
					String genre = genresArr[x].trim();
					if (genre != null && genre != "")
						query = query.where(qEntry.genres.likeIgnoreCase("%"+ genre +"%"));
				}						
			}
			
			Long totalCnt = query.fetchCount();
			
			query = query.restrict(new QueryModifiers(Long.valueOf(pageable.getPageSize()), Long.valueOf(pageable.getPageNumber())));
			List<AppEntity> entries = query.fetch();
			List<App> dtoList = entries.stream()
					.map(e -> appMapper.mapToDto(e))
					.collect(Collectors.toList());
			return new D360PageImpl<App>(dtoList, pageable, totalCnt);	
		});
	}

	
	@Transactional
	@Override
	public App addApp(App app) {
		AppEntity entity = appMapper.mapToEntity(app);
		entity = appRepository.saveAndFlush(entity);
		return appMapper.mapToDto(entity);
	}

	
	@Transactional
	@Override
	public App updateApp(App app) {
		AppEntity entity = appMapper.mapToEntity(app);
		entity = appRepository.saveAndFlush(entity);
		purgeCache(entity);
		return appMapper.mapToDto(entity);
	}
	
	
	@Override
	public void removeApp(App app) {
		AppEntity entity = appMapper.mapToEntity(app);
		appRepository.delete(entity);
		purgeCache(entity);		
	}
	
	private void purgeCache(AppEntity entity) {
		cache.evict(String.format(APP_BY_ID_KEY, entity.getId()));					
	}




}
