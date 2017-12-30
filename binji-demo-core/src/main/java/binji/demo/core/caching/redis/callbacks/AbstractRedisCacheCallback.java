package binji.demo.core.caching.redis.callbacks;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.types.Expiration;

import binji.demo.core.caching.redis.BinaryRedisCacheElement;
import binji.demo.core.caching.redis.RedisCacheMetadata;


/**
 * RedisCacheCallback enhanced implementation
 * Enables locking
 * See org.springframework.data.redis.cache.RedisCache.java for idea behind design.
 * This is a simplified approach.
 * 
 * @author jesse keane
 *
 */
public abstract class AbstractRedisCacheCallback<T>  implements RedisCallback<T> {

	private long WAIT_FOR_LOCK_TIMEOUT = 300;
	private long WAIT_TRIES = 10;
	private final BinaryRedisCacheElement element;
	private final RedisCacheMetadata cacheMetadata;
	private byte[] cacheLockKey;

	public AbstractRedisCacheCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
		this.element = element;
		this.cacheMetadata = metadata;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisCallback#doInRedis(org.springframework.data.redis.connection.RedisConnection)
	 */
	@Override
	public T doInRedis(RedisConnection connection) throws DataAccessException {
		return doInRedis(element, connection);
	}

	public abstract T doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException;
		
	protected void processKeyExpiration(RedisCacheElement element, RedisConnection connection) {
		if (!element.isEternal()) {
			connection.expire(element.getKeyBytes(), element.getTimeToLive());
		}
	}

	
	
	/*
	 *  Not necessary since we'll always use a prefix - jk 4/12/17
	 * 
	 * protected void maintainKnownKeys(RedisCacheElement element, RedisConnection connection) {

		if (!element.hasKeyPrefix()) {

			connection.zAdd(cacheMetadata.getSetOfKnownKeysKey(), 0, element.getKeyBytes());

			if (!element.isEternal()) {
				connection.expire(cacheMetadata.getSetOfKnownKeysKey(), element.getTimeToLive());
			}
		}
	}

	protected void cleanKnownKeys(RedisCacheElement element, RedisConnection connection) {

		if (!element.hasKeyPrefix()) {
			connection.zRem(cacheMetadata.getSetOfKnownKeysKey(), element.getKeyBytes());
		}
	}*/
	
	public byte[] getCacheLockKey() {
		if (cacheLockKey == null) {
			cacheLockKey = String.format(element.getKey().getKeyElement() + "~").getBytes();
		}
		return cacheLockKey;
	}

	protected boolean waitForLock(RedisConnection connection) {

		boolean retry;
		boolean foundLock = false;
		int tries = 0;
		do {
			retry = false;
			if (connection.exists(this.getCacheLockKey()) ) {
				foundLock = true;
				try {
					Thread.sleep(WAIT_FOR_LOCK_TIMEOUT);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				retry = true;
				tries++;
			}
			
		} while (retry);

		return foundLock;
	}

	protected void lock(RedisConnection connection) {
	
		String lockKey = String.format(element.getKey().getKeyElement() + "~");
		
		cacheLockKey = lockKey.getBytes();
		
		// expire lock as failsafe
		connection.set(this.getCacheLockKey(), "locked".getBytes(), Expiration.seconds(60), SetOption.UPSERT);
	}

	protected void unlock(RedisConnection connection) {
		connection.del(this.getCacheLockKey());
	}
}