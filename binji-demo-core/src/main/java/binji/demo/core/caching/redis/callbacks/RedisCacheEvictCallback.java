package binji.demo.core.caching.redis.callbacks;

import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;

import binji.demo.core.caching.redis.BinaryRedisCacheElement;
import binji.demo.core.caching.redis.RedisCacheMetadata;

/**
 * @author jesse keane
 *
 */
public class RedisCacheEvictCallback extends AbstractRedisCacheCallback<Void> {

	public RedisCacheEvictCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
		super(element, metadata);
	}
	
	@Override
	public Void doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {
		if (element.getKeyBytes() != null && element.getKey().getKeyElement() != "") 
			connection.del(element.getKeyBytes());			
		
		// if setKey exists, delete it's members and then itself
		if (element.getSetKeyBytes() != null) {
			Set<byte[]> keys = connection.sMembers(element.getSetKeyBytes());
			if (keys.isEmpty())
				return null;
			connection.del(keys.toArray(new byte[keys.size()][]));
			connection.del(element.getSetKeyBytes());
		}
			
		// don't need to clean known keys since we always use a prefix for the cache namespace
		//cleanKnownKeys(element, connection);
		return null;
	}

}
