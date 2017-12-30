package binji.demo.core.caching;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;


/**
 * Utility methods for cache keys
 * 
 * @author jesse keane
 *
 */
public class CacheKeyUtils {
	
	/**
	 * Key for caching
	 *  0 - page number
	 *  1 - page size
	 *  2 - sort direction
	 */
	private static final String PAGEABLE_KEY = "%s-%s-%s";

	
	/**
	 * Gets the cache key of the pageable object
	 * 
	 * @param pageable
	 * @return
	 */
	public static String getKey(Pageable pageable) {
		if (pageable == null) return null;
		
		return String.format(PAGEABLE_KEY, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
	}
	
	
	/**
	 * Gets the cache key of the list of longs, usually ids.
	 * 
	 * @param ids
	 * @return
	 */
	public static String getKey(List<Long> ids) {
		Collections.sort(ids);
		return ids.stream()
				.map(i -> i.toString())
				.collect(Collectors.joining(","));
	}
}
