package binji.demo.core.caching.redis;

import java.util.concurrent.Callable;

import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.serializer.RedisSerializer;

import lombok.Getter;

/**
 * Container for cache item
 * 
 * Extends Spring's {@link RedisCacheElement} to use our own {@link RedisCacheKeys} object
 * and allowing to specify serializer to use.
 * 
 * Note: Spring's design doesn't require the serializer at the container because they used an
 * additional container for the item's value itself which was a complexity that was 
 * removed for this implementation.
 * 
 * @author jesse keane
 *
 */
public class BinaryRedisCacheElement extends RedisCacheElement {
	
	private byte[] valueBytes;
	private boolean lazyLoad;
	@Getter
	private byte[] setKeyBytes;
	private final RedisSerializer valueSerializer;
	
	public BinaryRedisCacheElement(RedisCacheKeys cacheKeys, Object value, RedisSerializer valueSerializer) {
		super(cacheKeys, value);	
		this.valueSerializer = valueSerializer;
		this.lazyLoad = super.get() instanceof Callable;
		// if not lazyload, go ahead and serialize now
		this.valueBytes = lazyLoad ? null : valueSerializer.serialize(super.get());
		this.setKeyBytes = cacheKeys.getSetKeyBytes();
	}
	
	@Override
	public byte[] get() {

		if (lazyLoad && valueBytes == null) {
			try {
				valueBytes = valueSerializer.serialize(((Callable<?>)super.get()).call());
			} catch (Exception e) {
				throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e.getMessage(), e);
			}
		}
		return valueBytes;
	} 
	
	/* 
	 * Sets the expiration of the item in seconds.
	 * 
	 * Note: Overriden to support method chaining
	 * 
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.cache.RedisCacheElement#expireAfter(long)
	 */
	@Override
	public BinaryRedisCacheElement expireAfter(long seconds) {
		setTimeToLive(seconds);
		return this;
	}
}
