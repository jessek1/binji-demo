package binji.demo.core.caching.redis.callbacks;

import java.util.Arrays;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import binji.demo.core.caching.redis.BinaryRedisCacheElement;
import binji.demo.core.caching.redis.RedisCacheMetadata;
import binji.demo.core.caching.redis.RedisUtils;

public class RedisCacheCleanByPrefixCallback extends AbstractRedisCacheCallback<Void> {

	private static final byte[] REMOVE_KEYS_BY_PATTERN_LUA = new StringRedisSerializer().serialize(
			"local keys = redis.call('KEYS', ARGV[1]); local keysCount = table.getn(keys); if(keysCount > 0) then for _, key in ipairs(keys) do redis.call('del', key); end; end; return keysCount;");
	private static final byte[] WILD_CARD = new StringRedisSerializer().serialize("*");
	private final RedisCacheMetadata metadata;

	public RedisCacheCleanByPrefixCallback(RedisCacheMetadata metadata) {
		super(null, metadata);
		this.metadata = metadata;
	}

	@Override
	public Void doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {
		byte[] prefixToUse = Arrays.copyOf(metadata.getKeyPrefix(), metadata.getKeyPrefix().length + WILD_CARD.length);
		System.arraycopy(WILD_CARD, 0, prefixToUse, metadata.getKeyPrefix().length, WILD_CARD.length);
		if (RedisUtils.isClusterConnection(connection)) {
			// load keys to the client because currently Redis Cluster
			// connections do not allow eval of lua scripts.
			Set<byte[]> keys = connection.keys(prefixToUse);
			if (!keys.isEmpty()) {
				connection.del(keys.toArray(new byte[keys.size()][]));
			}
		} else {
			connection.eval(REMOVE_KEYS_BY_PATTERN_LUA, ReturnType.INTEGER, 0, prefixToUse);
		}
		return null;

	}

}
