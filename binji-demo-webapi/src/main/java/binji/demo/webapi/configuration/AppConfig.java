package binji.demo.webapi.configuration;

import java.util.List;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * @author jesse keane
 *
 */
@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(
  basePackages = {
    "binji.demo.webapi"   
  }
)
public class AppConfig extends WebMvcConfigurerAdapter{

	  
		@Bean
	    public static PropertyPlaceholderConfigurer getPropertyPlaceholderConfigurer()
	    {
	        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
	        ppc.setLocation(new ClassPathResource("binji-demo.properties"));
	        ppc.setIgnoreUnresolvablePlaceholders(true);
	        return ppc;
	    }
		
		
	    /**
	     * Configure ViewResolvers 
	     */
	    @Override
	    public void configureViewResolvers(ViewResolverRegistry registry) {
	 
	    	UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
	        viewResolver.setViewClass(JstlView.class);
	        viewResolver.setPrefix("/WEB-INF/jsp/");
	        viewResolver.setSuffix(".jsp");
	        registry.viewResolver(viewResolver);
	    }
	    
	    @Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/api/*")
					.allowedOrigins("*")
					.allowedMethods("GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE")
					.allowedHeaders("Access-Control-Allow-Origin", "Content-Type",
							"X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
							"Access-Control-Request-Headers", "Authorization");		
		}
	     
	   
	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        // registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	    }
	     
	    @Override
	    public void addFormatters(FormatterRegistry registry) {
	        //registry.addConverter(roleToUserProfileConverter);
	    }
	    
	 
	    @Override
	    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	      PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
	      //resolver.setMaxPageSize(10);
	      //resolver.setOneIndexedParameters(true);
	      resolver.setPageParameterName("p");
	      resolver.setSizeParameterName("s");
	      
	      argumentResolvers.add(resolver);
	       super.addArgumentResolvers(argumentResolvers);
	    }

	}
