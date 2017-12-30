package binji.demo.services.ovd;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQuery;

import binji.demo.common.dto.D360PageImpl;
import binji.demo.common.dto.ovd.OvdContentBase;
import binji.demo.common.dto.ovd.OvdContentEpisode;
import binji.demo.common.dto.ovd.OvdContentMovie;
import binji.demo.common.dto.ovd.OvdContentSeries;
import binji.demo.common.dto.ovd.OvdContentSpecial;
import binji.demo.common.dto.ovd.OvdContentSportsEvent;
import binji.demo.common.dto.ovd.OvdContentType;
import binji.demo.common.dto.ovd.OvdImage;
import binji.demo.core.caching.CacheKeyUtils;
import binji.demo.core.caching.D360Cache;
import binji.demo.data.entities.ovd.OvdContentEntity;
import binji.demo.data.entities.ovd.OvdFullVideoEntity;
import binji.demo.data.entities.ovd.QOvdContentEntity;
import binji.demo.data.entities.ovd.QOvdFullVideoEntity;
import binji.demo.data.entities.ovd.QOvdProviderEntity;
import binji.demo.data.repositories.ovd.OvdContentRepository;
import binji.demo.mapper.ovd.OvdContentMapper;

/**
 * @author jesse keane
 *
 */
public class OvdContentServiceImpl implements OvdContentService {
	
	private static final long DEFAULT_TTL = 1440 * 60;
	
	/**
	 * Key for caching 
	 *  0 - id
	*/	
	private static final String OVDCONTENT_BY_ID_KEY = "ovdcontent.id-%s";
	private static final long OVDCONTENT_BY_ID_TTL = DEFAULT_TTL;
	
	/**
	 * Key for caching 
	 *  0 - root id
	*/	
	private static final String OVDCONTENT_BY_ROOTID_KEY = "ovdcontent.rootid-%s";
	private static final long OVDCONTENT_BY_ROOTID_TTL = DEFAULT_TTL;
	
	/**
	 * Key for caching 
	 *  0 - tms id
	*/	
	private static final String OVDCONTENT_BY_TMSID_KEY = "ovdcontent.tmsid-%s";
	private static final long OVDCONTENT_BY_TMSID_TTL = DEFAULT_TTL;
	
	
	
	
	/**
	 * Key for caching 
	 *  0 - contentTypes
	 *  1 - title
	 *  2 - description
	 *  3 - beforeAirDate
	 *  4 - afterAirDate
	 *  5 - ratings
	 *  6 - genres
	 *  7 - ofTvLoadSeriesOnly
	 *  8 - provider
	 *  9 - pageable
	*/	
	private static final String OVDCONTENT_BY_CRITERIA_KEY = "ovdcontent.criteria-%s-%s-%s-%s-%s-%s-%s-%s-%s-%s";
	private static final long OVDCONTENT_BY_CRITERIA_TTL = DEFAULT_TTL;
	
	
	
	private final OvdContentRepository ovdContentRepository;
	private final OvdContentMapper ovdContentMapper;
	private final OvdImageService ovdImageService;
	private final D360Cache cache;
	private final boolean updateGracenoteImages;
	
	@PersistenceContext
	EntityManager entityManager;
	private final QOvdContentEntity qEntry = QOvdContentEntity.ovdContentEntity;
	private final QOvdFullVideoEntity qVideo = QOvdFullVideoEntity.ovdFullVideoEntity;
	private final QOvdProviderEntity qProvider = QOvdProviderEntity.ovdProviderEntity;
	
	
	public OvdContentServiceImpl(final OvdContentRepository ovdContentRepository,
			final OvdContentMapper ovdContentMapper,
			final OvdImageService ovdImageService,
			final CacheManager cacheManager,
			final boolean updateGracenoteImages) {
		this.ovdContentRepository = ovdContentRepository;
		this.ovdContentMapper = ovdContentMapper;
		this.ovdImageService = ovdImageService;
		this.cache = (D360Cache)cacheManager.getCache(OvdContentService.CACHE_NAME);
		this.updateGracenoteImages = updateGracenoteImages;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends OvdContentBase> T mapToDto(OvdContentEntity entity) {			
		T dto = null;
		switch(entity.getContentType()) {
		case SERIES:
			dto = (T) ovdContentMapper.mapToDtoSeries(entity);
			break;
		case EPISODE:
			dto =  (T) ovdContentMapper.mapToDtoEpisode(entity);
			break;
		case MOVIE:
			dto =  (T) ovdContentMapper.mapToDtoMovie(entity);
			break;
		case SPECIAL:
			dto =  (T) ovdContentMapper.mapToDtoSpecial(entity);
			break;
		case SPORT:
			dto =  (T) ovdContentMapper.mapToDtoSportsEvent(entity);
			break;
		default:
			return null;
		}
		return dto;
	}
	
	protected <T extends OvdContentBase> T processImages(T dto) {
		// check for images
		if (!updateGracenoteImages) return dto;
		
		if (dto.getImage() != null) {
			return dto;
		} else {
			// get from gn and then store it
			OvdImage img = ovdImageService.getOvdImageFromGracenote(dto.getTmsId(), dto.getContentType());		
			img.setOvdContentId(dto.getId());
			dto.setImage(img);			
			dto = updateOvdContent(dto);
		}
		return dto;
	}

	@Transactional
	@Override
	public OvdContentBase getOvdContentById(Long id) {
		String key = String.format(OVDCONTENT_BY_ID_KEY, id);
		
		return cache.get(key, OVDCONTENT_BY_ID_TTL, () -> {
			OvdContentEntity entity = ovdContentRepository.findOne(id);	
			return entity != null ? mapToDto(entity) : null;	
		});
	}

	/*@Override
	public OvdContentBase getOvdContentByRootId(Long rootId) {
		String key = String.format(OVDCONTENT_BY_ROOTID_KEY, rootId);
		
		return cache.get(key, OVDCONTENT_BY_ROOTID_TTL, () -> {
			OvdContentEntity entity = ovdContentRepository.findByRootId(rootId);	
			return entity != null ? mapToDto(entity) : null;	
		});
	}
	*/
	@Transactional
	@Override
	public <T extends OvdContentBase> T getOvdContentByTmsId(String tmsId) {
		String key = String.format(OVDCONTENT_BY_TMSID_KEY, tmsId);
		
		return cache.get(key, OVDCONTENT_BY_TMSID_TTL, () -> {
			OvdContentEntity entity = ovdContentRepository.findByTmsId(tmsId);	
			if (entity == null) return null;
			T dto = mapToDto(entity);
			dto = processImages(dto);
			return dto;
		});
	}
	
	/* 
	 * Used for data import processing resuming thus no cache and no image retrieval
	 * @see binji.demo.services.ovd.OvdContentService#getLastAddedOvdContent()
	 */
	@Transactional
	@Override
	public <T extends OvdContentBase> T getLastAddedOvdContent() {	
		OvdContentEntity entity = ovdContentRepository.findTopByOrderByIdDesc();	
		return entity != null ? mapToDto(entity) : null;		
	}
	
	
	
	
	@Transactional
	@Override
	public <T extends OvdContentBase> Page<T> findOvdContent(String contentTypes, String title, String description,
			Instant beforeAirDate, Instant afterAirDate, String ratings, String genres, boolean ofTvLoadSeriesOnly,
			String provider,
			Pageable pageable) {
		
		
		
		String key = String.format(OVDCONTENT_BY_CRITERIA_KEY, contentTypes, title, description, beforeAirDate,
				afterAirDate, ratings, genres, ofTvLoadSeriesOnly, provider, CacheKeyUtils.getKey(pageable));
		
		return cache.get(key, OVDCONTENT_BY_CRITERIA_TTL, () -> {
			JPAQuery<OvdContentEntity> query = new JPAQuery<OvdContentEntity>(this.entityManager);
			Path<String> value;
			
			query = query.from(qEntry)
					.leftJoin(qEntry.videos, qVideo)
					.leftJoin(qVideo.ovdProvider, qProvider);
			
			if (contentTypes != null && !contentTypes.isEmpty()) {
				String types[] = contentTypes.split("\\s*,\\s*");
				List<OvdContentType> typesParsed = new ArrayList<OvdContentType>();
				Arrays.stream(types).forEach(t -> {
					String type = t.trim().toUpperCase();
					if (type != null && type != "") {
						if (ofTvLoadSeriesOnly && type.equals("EPISODE")) {
						// skip
						} else {
							typesParsed.add(OvdContentType.valueOf(type));
						}
					}
				});			
				query = query.where(qEntry.contentType.in(typesParsed));
			}
			if (title != null && !title.isEmpty())
				query = query.where(qEntry.title.contains(title));
			if (description != null && !description.isEmpty())
				query = query.where(qEntry.description.contains(description));
			if (beforeAirDate != null)
				query = query.where(qEntry.airDate.loe(beforeAirDate));
			if (afterAirDate != null)
				query = query.where(qEntry.airDate.goe(afterAirDate));
			if (ratings != null && !ratings.isEmpty()) {
				String ratingsArr[] = ratings.split("\\s*,\\s*");
				List<String> ratingsTrimmed = new ArrayList<String>();
				Arrays.stream(ratingsArr).forEach(r -> {
					String rating = r.trim();
					if (rating != null && rating != "")
						ratingsTrimmed.add(rating);					
				});		
				query = query.where(qEntry.rating.in(ratingsTrimmed));
			}
			if (genres != null && !genres.isEmpty()) {
				String genresArr[] = genres.split("\\s*,\\s*");
				List<String> genresTrimmed = new ArrayList<String>();
				for (int x=0; x < genresArr.length; x++) {
					String genre = genresArr[x].trim();
					if (genre != null && genre != "")
						query = query.where(qEntry.genre.likeIgnoreCase("%"+ genre +"%"));
				}
			}
			
			if (provider != null && !provider.isEmpty()) {
				query = query.where(qProvider.name.likeIgnoreCase("%"+ provider +"%"));
			}
			
			query = query.where(qProvider.name.notIn(		
				"XFINITY", 
				"Netflix MX", 
				"YouTube MX",
				"Netflix DE",
				"Netflix UK",
				"YouTube CA",
				"YouTube UK",
				"Netflix CA",
				"iTunes CA",
				"iTunes UK",
				"ITV",
				"CBC",
				"CTV",
				"YouTube MX",
				"Netflix DE",
				"Netflix FR",
				"Netflix DK",
				"Netflix FI",
				"Netflix NO",
				"ZDF",
				"Arte",
				"Maxdome",
				"TF1",
				"FilmoTV",
				"France TV Pluzz VAD",
				"France TV Pluzz",
				"Wuaki ES",
				"6Play",
				"Chili Web",
				"ARD",
				"Amazon UK",
				"iTunes DE",
				"iTunes FR",
				"Videociety DE",
				"Netflix AT",
				"Viewster DE",
				"Viewster AT",
				"Crackle CA",
				"Netflix CH-fr",
				"Netflix CH-de",
				"Netflix BR",
				"TNT Go BR",
				"Space Go BR",
				"Telecine Play BR",
				"Crackle BR",
				"iTunes BR",
				"YouTube BR",
				"Cartoon Go BR",
				"Esporte Interativo",
				"Globosat GNT",
				"Globosat Universal",
				"Globosat SporTV",
				"Globosat Multishow",
				"Globosat BIS",
				"Globosat Viva",
				"Globosat Canal Off",
				"Globosat Gloob",
				"Globosat GloboNews",
				"Globosat Canal Brasil",
				"Globosat Megapix",
				"Globosat Combate",
				"Globosat Globosat",
				"Espn BR",
				"Philos",
				"7TV",
				"Sexy Hot",
				"Sky",
				"Netflix AU",
				"Fox Brazil Fox",
				"Fox Brazil FX",
				"Fox Brazil Sports",
				"Fox Brazil Life",
				"Fox Brazil National Geographic",
				"Netflix NZ",
				"iTunes AU",
				"Telstra",
				"SBS",
				"Stan",
				"Tenplay",
				"Wuaki FR",
				"Wuaki DE",
				"Wuaki IT",
				"Space Go MX",
				"TNT Go MX",
				"Fox Mexico Fox",
				"Fox Mexico Sports",
				"Fox Mexico FX",
				"Fox Mexico Life",
				"Fox Mexico National Geographic",
				"Fox Mexico Plus",
				"Wuaki UK",
				"PlusSeven",
				"NineNow",
				"ABCiView",
				"Globosat Syfy",
				"Fox Brazil Premium",
				"UKTV",
				"RTL TV Now",
				"HBO BR Documentaries",
				"HBO BR Kids",
				"HBO BR Movies",
				"HBO BR Series",
				"HBO BR Specials",
				"HBO BR Adult",
				"CW Seed",
				"Discovery Kids BR",
				"Foxtel Play",
				"Noggin BR",
				"AXN BR",
				"A&E BR",
				"E! BR",
				"Telemundo",
				"Sony BR",
				"History BR",
				"Lifetime BR",
				"Telekom Sport",
				"Netflix AU Telstra",
				"Fox Mexico Premium",
				"Hayu UK",
				"VM Store UK",
				"SevenPlus"));
			
			query.orderBy(qVideo.airTimeForSort.desc().nullsLast());
			query.orderBy(qEntry.year.desc().nullsLast());
		
			Long totalCnt = query.fetchCount();
			
			query = query.restrict(new QueryModifiers(Long.valueOf(pageable.getPageSize()), Long.valueOf(pageable.getPageNumber())));
			List<OvdContentEntity> entries = query.fetch();
			 
			entries = entries.stream().distinct().collect(Collectors.toList());
			
			@SuppressWarnings("unchecked")
			List<T> dtoList = entries.stream()
					.map(e -> {
						T dto = (T)mapToDto(e);
						dto = processImages(dto);
						return dto;
					})
					.collect(Collectors.<T>toList());		
			
		return new D360PageImpl<T>(dtoList, pageable, totalCnt);	
		});
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public <T extends OvdContentBase> T addOvdContent(T ovdContent) {
		OvdContentEntity entity = mapToEntity(ovdContent);
		entity = ovdContentRepository.saveAndFlush(entity);
		return (T) mapToDto(entity);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public <T extends OvdContentBase> T  updateOvdContent(T ovdContent) {
		OvdContentEntity entity = mapToEntity(ovdContent);
		entity = ovdContentRepository.saveAndFlush(entity);
		purgeCache(entity);
		return (T)mapToDto(entity);
	}
	
	private <T extends OvdContentBase> OvdContentEntity mapToEntity(T ovdContent) {
		OvdContentEntity entity = null;
		Class cls = ovdContent.getClass();
		if (cls == OvdContentEpisode.class)
			entity = ovdContentMapper.mapToEntity((OvdContentEpisode)ovdContent);
		else if (cls == OvdContentMovie.class)
			entity = ovdContentMapper.mapToEntity((OvdContentMovie)ovdContent);
		else if (cls == OvdContentSeries.class)
			entity = ovdContentMapper.mapToEntity((OvdContentSeries)ovdContent);
		else if (cls == OvdContentSpecial.class)
			entity = ovdContentMapper.mapToEntity((OvdContentSpecial)ovdContent);
		else if (cls == OvdContentSportsEvent.class)
			entity = ovdContentMapper.mapToEntity((OvdContentSportsEvent)ovdContent);
		return entity;
	}

	@Override
	public void removeOvdContent(OvdContentBase ovdContent) {
		// TODO Auto-generated method stub
		
	}
	
	private void purgeCache(OvdContentEntity entity) {
		cache.evict(String.format(OVDCONTENT_BY_ID_KEY, entity.getId()));	
		cache.evict(String.format(OVDCONTENT_BY_ROOTID_KEY, entity.getRootId()));			
	}

}
