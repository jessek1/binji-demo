package binji.demo.dataimport.configuration;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import binji.demo.core.configuration.CacheConfig;
import binji.demo.core.serializers.JacksonSerializer;
import binji.demo.core.serializers.Serializer;
import binji.demo.data.JpaConfig;
import binji.demo.dataimport.application.Application;
import binji.demo.services.ServicesConfig;
import binji.demo.services.xml.XmlIngestService;

/**
 * @author jesse keane
 *
 */
@Configuration
@ComponentScan(basePackages = {"binji.demo.services", "binji.demo.data",
		"binji.demo.mapper", "binji.demo.core"})
public class ImportConfig extends CachingConfigurerSupport implements EnvironmentAware {

	private static Logger LOGGER = Logger.getLogger(ImportConfig.class);
	
	private Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;				
	}
	
	
	@Bean
	public Application importXml() {
		return new Application();
	}
	
	@Bean
    public Serializer serializer() { return new JacksonSerializer(); }
    

		
}
