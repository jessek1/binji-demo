package binji.demo.core.caching.redis.callbacks;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;

import binji.demo.core.caching.redis.BinaryRedisCacheElement;
import binji.demo.core.caching.redis.RedisCacheMetadata;
import binji.demo.core.caching.redis.RedisUtils;


public class RedisWriteThroughCallback extends AbstractRedisCacheCallback<byte[]> {
	
	private static Logger LOGGER = Logger.getLogger(RedisWriteThroughCallback.class);
	

	public RedisWriteThroughCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
		super(element, metadata);
	}
	
	@Override
	public byte[] doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {	
		try {
			waitForLock(connection);
			byte[] value = connection.get(element.getKeyBytes());
			if (value != null) {
				LOGGER.debug(String.format("Cache hit, key = %s", element.getKey().getKeyElement()));
				return value;
			}
			LOGGER.debug(String.format("Cache miss, key = %s", element.getKey().getKeyElement()));
			
			lock(connection);
			if (!RedisUtils.isClusterConnection(connection)) {
				connection.watch(element.getKeyBytes());
				connection.multi();
			}			
			value = element.get();
			if (value.length == 0) {
				connection.del(element.getKeyBytes());
			} else {					
				connection.set(element.getKeyBytes(), value);
				processKeyExpiration(element, connection);
				// don't need to maintain known keys since we always use a prefix for the cache namespace
				// maintainKnownKeys(element, connection);
				
				// add element's key to SET if setKey exists
				if (element.getSetKeyBytes() != null)
					connection.sAdd(element.getSetKeyBytes(), element.getKeyBytes());
			}
			if (!RedisUtils.isClusterConnection(connection)) {
				connection.exec();
			}
			return value;
		} catch (RuntimeException e) {
			if (!RedisUtils.isClusterConnection(connection)) {
				connection.discard();
			}
			throw e;
		} finally {
			unlock(connection);
		}
	}
}
