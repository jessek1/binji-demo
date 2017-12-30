package binji.demo.services;



import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import binji.demo.core.httpclient.gracenote.GracenoteClient;
import binji.demo.core.serializers.Serializer;
import binji.demo.data.repositories.apps.AppRepository;
import binji.demo.data.repositories.ovd.OvdContentRepository;
import binji.demo.data.repositories.ovd.OvdImageRepository;
import binji.demo.data.repositories.ovd.OvdProviderRepository;
import binji.demo.mapper.apps.AppMapper;
import binji.demo.mapper.ovd.OvdContentMapper;
import binji.demo.mapper.ovd.OvdImageMapper;
import binji.demo.mapper.ovd.OvdProviderMapper;
import binji.demo.services.apps.AppService;
import binji.demo.services.apps.AppServiceImpl;
import binji.demo.services.csv.CsvIngestService;
import binji.demo.services.csv.CsvIngestServiceImpl;
import binji.demo.services.ovd.GracenoteService;
import binji.demo.services.ovd.GracenoteServiceImpl;
import binji.demo.services.ovd.OvdContentService;
import binji.demo.services.ovd.OvdContentServiceImpl;
import binji.demo.services.ovd.OvdImageService;
import binji.demo.services.ovd.OvdImageServiceImpl;
import binji.demo.services.ovd.OvdProviderService;
import binji.demo.services.ovd.OvdProviderServiceImpl;
import binji.demo.services.xml.XmlIngestService;
import binji.demo.services.xml.XmlIngestServiceImpl;


/**
 * @author jesse keane
 *
 */
@Configuration
@PropertySource(value = { "classpath:binji-demo.properties" })
public class ServicesConfig implements EnvironmentAware {
	private static Logger LOGGER = Logger.getLogger(ServicesConfig.class);

	private Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {
		LOGGER.debug(String.format("Setting environment as %s",environment));
		this.environment = environment;				
	}

	@Bean
	public XmlIngestService xmlIngestService(Jaxb2Marshaller jax, 
					OvdContentService ovdService,
					OvdProviderService ovdProviderService,
					Serializer serializer,
					JAXBContext jaxContext) {		
		return new XmlIngestServiceImpl(jax, ovdService, ovdProviderService, serializer, jaxContext);
	}
	
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller jax = new Jaxb2Marshaller();
		jax.setPackagesToScan("binji.demo.data.xml");
		return jax;
	}
	
	@Bean
	public JAXBContext jaxbContext() {
		try {
			return JAXBContext.newInstance("binji.demo.data.xml.ovd");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Bean
	public OvdContentService ovdContentService(OvdContentRepository ovdContentRepository,
			OvdContentMapper ovdContentMapper,
			OvdImageService ovdImageService,
			CacheManager cacheManager) {		
		return new OvdContentServiceImpl(ovdContentRepository,
				ovdContentMapper,
				ovdImageService,
				cacheManager,
				Boolean.parseBoolean(environment.getProperty("binji.gracenote.images.alwaysupdate")));
	}
	
	@Bean
	public OvdImageService ovdImageService(OvdImageRepository ovdImageRepository,
			OvdImageMapper ovdImageMapper,
			GracenoteService gracenoteService,
			CacheManager cacheManager) {
		return new OvdImageServiceImpl(ovdImageRepository, ovdImageMapper, gracenoteService, cacheManager);
	}
	
	@Bean
	public OvdProviderService ovdProviderService(OvdProviderRepository ovdProviderRepository,
			OvdProviderMapper ovdProviderMapper,
			CacheManager cacheManager) {
		return new OvdProviderServiceImpl(ovdProviderRepository, ovdProviderMapper, cacheManager);
	}
	
	@Bean
	public CsvIngestService csvIngestService(AppService appService) {
		return new CsvIngestServiceImpl(appService);
	}
	
	@Bean
	public AppService appService(AppRepository appRepository,
			AppMapper appMapper,
			CacheManager cacheManager) {		
		return new AppServiceImpl(appRepository,
				appMapper,
				cacheManager);
	}
	
	@Bean
	public GracenoteService gracenoteService(GracenoteClient client, Serializer serializer) {
		return new GracenoteServiceImpl(client, serializer,
				environment.getRequiredProperty("binji.gracenote.images.baseurl"),
				environment.getRequiredProperty("binji.gracenote.api.key"));
	}
	
	    

}
