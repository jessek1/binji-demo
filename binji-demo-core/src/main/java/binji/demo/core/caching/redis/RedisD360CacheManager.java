package binji.demo.core.caching.redis;

import java.util.Collection;
import java.util.Collections;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import binji.demo.core.caching.D360Cache;


/**
 * Cache manager to return RedisD360Cache implementation
 * 
 * @author jesse keane
 *
 */
public class RedisD360CacheManager extends AbstractTransactionSupportingCacheManager  {
	
	private final RedisTemplate redisTemplate;
	private final String prefix;
	
	public RedisD360CacheManager(RedisTemplate redisTemplate, String prefix) {
		this.redisTemplate = redisTemplate;
		this.prefix = prefix + ".";
	}

	

	@Override
	protected Collection<? extends Cache> loadCaches() {
		// currently not supporting initialization of cache on first cache use - jk 4/12/17
		return Collections.emptyList();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.springframework.cache.support.AbstractCacheManager#getMissingCache(java.lang.String)
	 */
	@Override
	protected Cache getMissingCache(String name) {
		return createCache(name);
	}
	
	
	protected D360Cache createCache(String cacheName) {		
		return new RedisD360Cache(prefix.getBytes(), cacheName, redisTemplate);
	}
	
}
