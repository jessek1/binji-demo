package binji.demo.services.apps;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import binji.demo.common.dto.apps.App;

/**
 * App service interface
 * 
 * @author jesse keane
 *
 */
public interface AppService {
	
	public static final String CACHE_NAME = "app";
	
	App getAppById(Long id);
	
	App getAppByTitle(String title);
	
	Page<App> findApps(String title, String description, String genres, Pageable pageable);
	
	App addApp(App app);
	
	App  updateApp(App app);
	
	void removeApp(App app);
}
