package binji.demo.services.ovd;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;

import binji.demo.common.dto.ovd.OvdContentType;
import binji.demo.common.dto.ovd.OvdImage;
import binji.demo.common.model.gracenote.ProgramImage;
import binji.demo.core.caching.D360Cache;
import binji.demo.data.entities.ovd.OvdImageEntity;
import binji.demo.data.entities.ovd.OvdProviderEntity;
import binji.demo.data.repositories.ovd.OvdImageRepository;
import binji.demo.mapper.ovd.OvdImageMapper;

/**
 * @author jesse keane
 *
 */
public class OvdImageServiceImpl implements OvdImageService {

	private static final long DEFAULT_TTL = 1440 * 60;

	/**
	 * Key for caching 0 - id
	 */
	private static final String OVDIMAGE_BY_ID_KEY = "ovdimage.id-%s";
	private static final long OVDIMAGE_BY_ID_TTL = DEFAULT_TTL;

	private final OvdImageRepository ovdImageRepository;
	private final OvdImageMapper ovdImageMapper;
	private final GracenoteService gracenoteService;
	private final D360Cache cache;

	public OvdImageServiceImpl(final OvdImageRepository ovdImageRepository, final OvdImageMapper ovdImageMapper,
			final GracenoteService gracenoteService, final CacheManager cacheManager) {
		this.ovdImageRepository = ovdImageRepository;
		this.ovdImageMapper = ovdImageMapper;
		this.gracenoteService = gracenoteService;
		this.cache = (D360Cache) cacheManager.getCache(OvdContentService.CACHE_NAME);
	}

	@Override
	public OvdImage getOvdImageById(Long id) {
		String key = String.format(OVDIMAGE_BY_ID_KEY, id);

		return cache.get(key, OVDIMAGE_BY_ID_TTL, () -> {
			OvdImageEntity entity = ovdImageRepository.findOne(id);
			return entity != null ? ovdImageMapper.mapToDto(entity) : null;
		});
	}

	public OvdImage getOvdImageFromGracenote(String tmsId, OvdContentType contentType) {
		List<ProgramImage> images = gracenoteService.getProgramImages(tmsId);

		ProgramImage thumb = null;
		ProgramImage detail = null;
		ProgramImage bgBanner = null;

		switch (contentType) {
		case SERIES:
		case EPISODE:
			List<ProgramImage> seriesImages = images.stream()
					.filter(i -> (i.getPrimary() != null && i.getPrimary().equalsIgnoreCase("true"))
							&& (i.getTier() != null && i.getTier().equalsIgnoreCase("series"))
							&& (i.getAspect() != null && i.getAspect().equalsIgnoreCase("4x3"))
							&& (i.getCategory() != null && i.getCategory().startsWith("Banner")))
					.collect(Collectors.toList());
			thumb = seriesImages.stream().filter(i -> i.getSize().equalsIgnoreCase("Md")).findAny().orElse(null);
			detail = seriesImages.stream().filter(i -> i.getSize().equalsIgnoreCase("Lg")).findAny().orElse(null);
			if (detail == null)
				detail = seriesImages.stream().filter(i -> i.getSize().equalsIgnoreCase("Md")).findAny().orElse(null);
			bgBanner = images.stream()
					.filter(i -> (i.getPrimary() != null && i.getPrimary().equalsIgnoreCase("true"))
							&& (i.getCategory() != null && i.getCategory().equalsIgnoreCase("Iconic"))
							&& (i.getAspect() != null && i.getAspect().equalsIgnoreCase("16x9"))
							&& (i.getSize() != null && i.getSize().equalsIgnoreCase("Lg"))
							&& (i.getWidth() != null && i.getWidth().equalsIgnoreCase("1280")))
					.findAny().orElse(null);
		
			break;
		case MOVIE:
			List<ProgramImage> vodImages = images.stream()
					.filter(i -> (i.getPrimary() != null && i.getPrimary().equalsIgnoreCase("true"))
							&& (i.getCategory() != null && i.getCategory().equalsIgnoreCase("VOD Art"))
							&& (i.getAspect() != null && i.getAspect().equalsIgnoreCase("4x3")))
					.collect(Collectors.toList());
			thumb = vodImages.stream().filter(i -> i.getSize().equalsIgnoreCase("Md")).findAny().orElse(null);
			if (thumb == null) 
				thumb = vodImages.stream().filter(i -> i.getSize().equalsIgnoreCase("Ms")).findAny().orElse(null);	
							
			detail = vodImages.stream().filter(i -> i.getSize().equalsIgnoreCase("Lg")).findAny().orElse(null);
			if (detail == null)
				detail = thumb;
			
			bgBanner = images.stream()
					.filter(i -> (i.getPrimary() != null && i.getPrimary().equalsIgnoreCase("true"))
							&& (i.getCategory() != null && i.getCategory().equalsIgnoreCase("Iconic"))
							&& (i.getAspect() != null && i.getAspect().equalsIgnoreCase("16x9"))
							&& (i.getSize() != null && i.getSize().equalsIgnoreCase("Lg"))
							&& (i.getWidth() != null && i.getWidth().equalsIgnoreCase("1280")))
					.findAny().orElse(null);
			break;
		case SPECIAL:
			break;
		case SPORT:
			break;
		default:
			break;
		}

		OvdImage ovdImage = new OvdImage();
		if (thumb != null)
			ovdImage.setThumb(gracenoteService.getImageAbsoluteUrl(thumb.getUri()));
		if (detail != null)
			ovdImage.setDetail(gracenoteService.getImageAbsoluteUrl(detail.getUri()));
		if (bgBanner != null)
			ovdImage.setBgBanner(gracenoteService.getImageAbsoluteUrl(bgBanner.getUri()));

		return ovdImage;
	}

	/*
	 * @Override public OvdImage addOvdImage(OvdImage ovdImage) { OvdImageEntity
	 * entity = ovdImageMapper.mapToEntity(ovdImage); entity =
	 * ovdImageRepository.saveAndFlush(entity); return
	 * ovdImageMapper.mapToDto(entity); }
	 * 
	 * @Override public OvdImage updateOvdImage(OvdImage ovdImage) {
	 * OvdImageEntity entity = ovdImageMapper.mapToEntity(ovdImage); entity =
	 * ovdImageRepository.saveAndFlush(entity); return
	 * ovdImageMapper.mapToDto(entity); }
	 * 
	 * @Override public void removeOvdImage(OvdImage ovdImage) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 */

}
