package binji.demo.core.caching.redis.callbacks;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;

import binji.demo.core.caching.redis.BinaryRedisCacheElement;
import binji.demo.core.caching.redis.RedisCacheMetadata;
import binji.demo.core.caching.redis.RedisUtils;

public class RedisCachePutCallback extends AbstractRedisCacheCallback<Void> {

	public RedisCachePutCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
		super(element, metadata);
	}
	
	@Override
	public Void doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {
		if (!RedisUtils.isClusterConnection(connection)) {
			connection.multi();
		}
		if (element.get().length == 0) {
			connection.del(element.getKeyBytes());
		} else {
			connection.set(element.getKeyBytes(), element.get());
			processKeyExpiration(element, connection);
			// don't need to maintain known keys since we always use a prefix for the cache namespace
			//maintainKnownKeys(element, connection);
			
			// add element's key to SET if setKey exists
			if (element.getSetKeyBytes() != null)
				connection.sAdd(element.getSetKeyBytes(), element.getKeyBytes());
		}
		if (!RedisUtils.isClusterConnection(connection)) {
			connection.exec();
		}
		return null;
	}

}
