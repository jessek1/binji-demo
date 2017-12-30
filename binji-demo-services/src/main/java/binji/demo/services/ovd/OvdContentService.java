package binji.demo.services.ovd;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import binji.demo.common.dto.ovd.OvdContentBase;

/**
 * Ovd content service interface
 * 
 * @author jesse keane
 *
 */
public interface OvdContentService {
	
	public static final String CACHE_NAME = "content";
	
	OvdContentBase getOvdContentById(Long id);
	
	/*OvdContentBase getOvdContentByRootId(Long rootId);*/
	<T extends OvdContentBase> T getOvdContentByTmsId(String tmsId);
	
	<T extends OvdContentBase> T getLastAddedOvdContent();

	
	
	<T extends OvdContentBase> T addOvdContent(T ovdContent);
	
	<T extends OvdContentBase> T  updateOvdContent(T ovdContent);
	
	void removeOvdContent(OvdContentBase ovdContent);

	<T extends OvdContentBase> Page<T> findOvdContent(String contentTypes, String title, String description, Instant beforeAirDate,
			Instant afterAirDate, String ratings, String genres, boolean ofTvLoadSeriesOnly, String provider,
			Pageable pageable);

	

	
}
