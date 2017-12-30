package binji.demo.core.caching.redis;

import java.util.Arrays;

import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Extends a RedisCacheKey to allow for a setKey to be defined along with a regular cache key
 * 
 * @author jesse keane
 *
 */
public class RedisCacheKeys extends RedisCacheKey {

	private byte[] prefix;
	private final String setKey;
		
	public RedisCacheKeys(Object keyElement, String setKey ) {
		super(keyElement != null ? keyElement : "");
			
		this.setKey = setKey;
	}
	
	public byte[] getSetKeyBytes() {

		if (setKey == null)
			return null;
		
		byte[] rawKey = setKey.getBytes();
		if (!hasPrefix()) {
			return rawKey;
		}

		byte[] prefixedKey = Arrays.copyOf(prefix, prefix.length + rawKey.length);
		System.arraycopy(rawKey, 0, prefixedKey, prefix.length, rawKey.length);

		return prefixedKey;
	}
	
	
	/* 
	 * Note: Overriden to support method chaining.
	 * Overriden so that this object can have access to prefix.  Prefix is private in the base class.
	 * 
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.cache.RedisCacheKey#usePrefix(byte[])
	 */
	@Override
	public RedisCacheKeys usePrefix(byte[] prefix) {
		super.usePrefix(prefix);
		this.prefix = prefix;		
		return this;
	}
	
	
	/* 
	 * Sets serializer to use for cache keys
	 * Note: Overriden to support method chaining.
	 * 
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.cache.RedisCacheKey#withKeySerializer(org.springframework.data.redis.serializer.RedisSerializer)
	 */
	@Override
	public RedisCacheKeys withKeySerializer(RedisSerializer serializer) {
		super.withKeySerializer(serializer);
		return this;
	}

}
