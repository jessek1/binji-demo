package binji.demo.core.configuration;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import binji.demo.core.httpclient.OkHttpClientImpl;
import binji.demo.core.httpclient.gracenote.GracenoteClient;
import binji.demo.core.httpclient.gracenote.api.ProgramImagesClient;
import binji.demo.core.serializers.JacksonSerializer;
import binji.demo.core.serializers.Serializer;
import okhttp3.OkHttpClient;


/**
 * @author jesse keane
 *
 */
@Configuration
@ComponentScan(basePackages = {"binji.demo.services", "binji.demo.data",
		"binji.demo.mapper", "binji.demo.core"})
public class RootConfig implements EnvironmentAware {

	private Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;				
	}
		
	 
    @Bean
    public Serializer serializer() { return new JacksonSerializer(); }
    
    @Bean
    public GracenoteClient gracenoteClient(ProgramImagesClient programImagesClient) {
    	return new GracenoteClient(programImagesClient);
    }
    
    @Bean
    public ProgramImagesClient programImagesClient(OkHttpClientImpl httpClient) {
    	return new ProgramImagesClient(httpClient,
			environment.getRequiredProperty("binji.gracenote.api.baseurl"),
			environment.getRequiredProperty("binji.gracenote.api.key"));
    }
    
    @Bean
    @Scope(value="singleton")
    public OkHttpClientImpl okHttpClient(Serializer serializer) {
    	return new OkHttpClientImpl(serializer, new OkHttpClient());
    }
    
}
