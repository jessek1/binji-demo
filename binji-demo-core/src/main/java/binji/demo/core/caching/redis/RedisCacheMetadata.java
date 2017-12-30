package binji.demo.core.caching.redis;

import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

import lombok.Getter;
import lombok.Setter;

/**
 * Metadata required to maintain RedisCache. Keeps track of additional data structures required for processing
 * cache operations.
 *
 * @author jesse keane
 * 
 */
@Getter
@Setter
public class RedisCacheMetadata {
	private final String cacheName;
	private final byte[] keyPrefix;
	private final byte[] setOfKnownKeys;
	private final byte[] cacheLockName;
	private long defaultExpiration = 0;
	
	public RedisCacheMetadata(String cacheName, byte[] keyPrefix) {
		
		Assert.hasText(cacheName, "CacheName must not be null or empty!");
		this.cacheName = cacheName;
		this.keyPrefix = keyPrefix;

		StringRedisSerializer stringSerializer = new StringRedisSerializer();

		// name of the set holding the keys
		this.setOfKnownKeys = usesKeyPrefix() ? new byte[] {} : stringSerializer.serialize(cacheName + "~keys");
		this.cacheLockName = stringSerializer.serialize(cacheName + "~lock");
	}
	
	public boolean usesKeyPrefix() {
		return (keyPrefix != null && keyPrefix.length > 0);
	}
}
