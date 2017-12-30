package binji.demo.core.caching.redis;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import binji.demo.core.caching.D360Cache;
import binji.demo.core.caching.redis.callbacks.RedisCacheCleanByPrefixCallback;
import binji.demo.core.caching.redis.callbacks.RedisCacheEvictCallback;
import binji.demo.core.caching.redis.callbacks.RedisCachePutCallback;
import binji.demo.core.caching.redis.callbacks.RedisCachePutIfAbsentCallback;
import binji.demo.core.caching.redis.callbacks.RedisCachePutToSetCallback;
import binji.demo.core.caching.redis.callbacks.RedisWriteThroughCallback;
import lombok.Getter;


/**
 * Cache implementation based upon Sping's redis cache implementation.
 * 
 * Adds ability to provide a setKey along with an item's key to be stored in a set.
 * The set can be used to performantly evict the keys stored as members within it.
 * This allows us to maintain an index of cache keys related to the same entity but
 * with different variations such as filters, paging, etc.
 * 
 * @author jesse keane
 *
 */
public class RedisD360Cache implements D360Cache {
	
	private static Logger LOGGER = Logger.getLogger(RedisD360Cache.class);
	
	@Getter
	private final byte[] prefix;
	@Getter
	private final String name;
	@Getter
	private final long expiration;
	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisSerializer valueSerializer;
	private final RedisCacheMetadata cacheMetadata;

	public RedisD360Cache(byte[] prefix, String name, RedisTemplate redisTemplate) {
		this(prefix, name, redisTemplate, 300);
	}
	
	public RedisD360Cache(byte[] prefix, String name, RedisTemplate redisTemplate, long expiration) {
		this.redisTemplate = redisTemplate;
		this.prefix = prefix;
		this.name = name;
		this.expiration = expiration;
		this.valueSerializer = redisTemplate.getValueSerializer();
		this.cacheMetadata = new RedisCacheMetadata(name, prefix);
		this.cacheMetadata.setDefaultExpiration(expiration);
	}
	
	
	@Override
	public <T> T get(Object key, long expiration, Callable<T> valueLoader) {
		return get(key, null, expiration, valueLoader);
	}
	

	/**
	 * Gets the item from cache and provides a setKey for the result to be added to, if item is loaded from valueLoader
	 * 
	 * @param key
	 * @param setKey 
	 * @param valueLoader
	 * @return
	 */
	public <T> T get(Object key, String setKey, long expiration, Callable<T> valueLoader) {
		
		RedisCacheKeys cacheKeys = getRedisCacheKeys(key, setKey);
		Object val = get(cacheKeys);
		if (val != null) {
			return (T) val;
		}
		
		BinaryRedisCacheElement rce = new BinaryRedisCacheElement(cacheKeys, valueLoader, valueSerializer)
				.expireAfter(expiration);
		RedisWriteThroughCallback callback = new RedisWriteThroughCallback(rce, cacheMetadata);

		try {
			byte[] data = (byte[]) redisTemplate.execute(callback);
			if (data == null) {				
				return null;
			} else {				
				return (T) valueSerializer.deserialize(data);
			}			
		} catch (RuntimeException e) {
			throw new RedisSystemException(
					String.format("Value for key '%s' could not be loaded using '%s'.", key, valueLoader), e);
		}
	}
	
	public void put(Object key, String setKey, long expiration, Object value) {
		BinaryRedisCacheElement rce = new BinaryRedisCacheElement(getRedisCacheKeys(key, setKey), value, valueSerializer)
				.expireAfter(expiration);
		redisTemplate.execute(new RedisCachePutCallback(rce, cacheMetadata));
	}

	public String getCacheKey(Object key) {
		if (key == null) return null; // return null if the caller is trying to not set a key
		return String.format("%s.%s",  name, key);
				
	}
	

	@Override
	public Object getNativeCache() {
		return redisTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ValueWrapper get(Object key) {
		
		Object result = get(getRedisCacheKeys(key, null));
		
		SimpleValueWrapper wrapper = null;
		if (result != null) {
			wrapper = new SimpleValueWrapper(result);
		}
		return wrapper;
		
		
	}
	
	@SuppressWarnings("unchecked")
	public Object get(RedisCacheKeys cacheKeys) {
		// TODO - do deeper discovery on performance impact of using RedisCallback to use a connection 
		//		  exec EXISTS and GET commands versus just getting with the higher level opsForValue method. jk 4/10/17
		Object result = redisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection conn) throws DataAccessException {
				byte[] keyBytes = cacheKeys.getKeyBytes();
				
				if (!conn.exists(keyBytes)) {
					return null;
				}
				
				return valueSerializer.deserialize(conn.get(keyBytes));
			}
		});
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		ValueWrapper wrapper = get(key);
		return wrapper == null ? null : (T)wrapper.get();
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		return get(key, null, expiration, valueLoader);
	}

	@Override
	public void put(Object key, Object value) {
		put(key, null, expiration, value);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		BinaryRedisCacheElement rce = new BinaryRedisCacheElement(getRedisCacheKeys(key, null), value, valueSerializer);
		RedisCachePutIfAbsentCallback callback = 
				new RedisCachePutIfAbsentCallback(rce, cacheMetadata);
		

		byte[] resultBytes = (byte[]) redisTemplate.execute(callback);
		SimpleValueWrapper wrapper = null;
		if (resultBytes != null) {
			wrapper = new SimpleValueWrapper(valueSerializer.deserialize(resultBytes));
		}
		return wrapper;
		
	}

	@Override
	public void evict(Object key) {
		BinaryRedisCacheElement rce = new BinaryRedisCacheElement(getRedisCacheKeys(key, null), null, valueSerializer);
		evict(rce);
	}
	
	@Override
	public void evictSet(String setKey) {
		BinaryRedisCacheElement rce = new BinaryRedisCacheElement(getRedisCacheKeys(null, setKey), null, valueSerializer);
		evict(rce);
	}
	
	public void evict(BinaryRedisCacheElement rce) {
		RedisCacheEvictCallback callback = new RedisCacheEvictCallback(rce, cacheMetadata);
		redisTemplate.execute(callback);
	}

	@Override
	public void clear() {
		redisTemplate.execute(new RedisCacheCleanByPrefixCallback(cacheMetadata));
	}
	
	private RedisCacheKeys getRedisCacheKeys(Object key, String setKey) {
		return new RedisCacheKeys(getCacheKey(key), getCacheKey(setKey)).usePrefix(this.cacheMetadata.getKeyPrefix())
				.withKeySerializer(redisTemplate.getKeySerializer());
	}

	
	
	/* (non-Javadoc)
	 * @see net.totempower.totemnet.core.caching.TotemCache#putToSet(java.lang.String, java.lang.String)
	 */
	public void putToSet(String key, String setKey) {
		BinaryRedisCacheElement rce = new BinaryRedisCacheElement(getRedisCacheKeys(key, setKey), null, valueSerializer);
		putToSet(rce);
	}
	
	public void putToSet(BinaryRedisCacheElement rce) {
		redisTemplate.execute(new RedisCachePutToSetCallback(rce, cacheMetadata));
	}

	
}
