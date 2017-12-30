package binji.demo.core.caching.redis.callbacks;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;

import binji.demo.core.caching.redis.BinaryRedisCacheElement;
import binji.demo.core.caching.redis.RedisCacheMetadata;

public class RedisCachePutIfAbsentCallback extends AbstractRedisCacheCallback<byte[]> {

	public RedisCachePutIfAbsentCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
		super(element, metadata);
	}
	
	@Override
	public byte[] doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {
		
		byte[] keyBytes = element.getKeyBytes();
		byte[] value = element.get();
		if (!connection.setNX(keyBytes, value)) {
			return connection.get(keyBytes);
		}
		// don't need to maintain known keys since we always use a prefix for the cache namespace
		// maintainKnownKeys(element, connection);
		processKeyExpiration(element, connection);
		
		// add element's key to SET if setKey exists
		if (element.getSetKeyBytes() != null)
			connection.sAdd(element.getSetKeyBytes(), element.getKeyBytes());
		
		return null;
	}

}
