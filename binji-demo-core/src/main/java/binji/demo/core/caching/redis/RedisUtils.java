package binji.demo.core.caching.redis;

import org.springframework.data.redis.connection.DecoratedRedisConnection;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;

/**
 * @author jesse keane
 *
 */
public class RedisUtils {
	public static boolean isClusterConnection(RedisConnection connection) {
		while (connection instanceof DecoratedRedisConnection) {
			connection = ((DecoratedRedisConnection) connection).getDelegate();
		}
		return connection instanceof RedisClusterConnection;
	}
}
