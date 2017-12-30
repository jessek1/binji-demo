package binji.demo.core.configuration;

import org.apache.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import binji.demo.core.caching.redis.CustomGenericJackson2JsonRedisSerializer;
import binji.demo.core.caching.redis.RedisD360CacheManager;


/**
 * @author jesse keane
 *
 */
@Configuration
@PropertySource(value = { "classpath:binji-demo.properties" })
public class CacheConfig extends CachingConfigurerSupport implements EnvironmentAware {

	private static Logger LOGGER = Logger.getLogger(CacheConfig.class);
	private static final String CACHE_NAMESPACE_PREFIX = "binji";
	private Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;				
	}
		
	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
		redisConnectionFactory.setHostName(environment.getRequiredProperty("redis.host"));
		redisConnectionFactory.setPort(Integer.parseInt(environment.getRequiredProperty("redis.port")));
		redisConnectionFactory.setUsePool(true);
		return redisConnectionFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(cf);
		redisTemplate.setKeySerializer(keySerializer());
		redisTemplate.setValueSerializer(valueSerializer());		
		return redisTemplate;
	}
	
	@Bean
	public RedisSerializer keySerializer() {
		return new StringRedisSerializer();
	}
	
	@Bean
	public RedisSerializer valueSerializer() {
		return new CustomGenericJackson2JsonRedisSerializer();		
	}

	@Bean
	public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
		return new RedisD360CacheManager(redisTemplate, CACHE_NAMESPACE_PREFIX);
	}
	
	
	
	


}
