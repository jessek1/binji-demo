package binji.demo.webapi.servlet;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import binji.demo.common.dto.apps.App;
import binji.demo.common.dto.ovd.OvdContentBase;
import binji.demo.services.apps.AppService;
import binji.demo.services.ovd.OvdContentService;

/**
 * @author jesse keane
 *
 */
@Controller
@EnableWebMvc
@RequestMapping("/v0.0.0")
public class AppController {
	private static Logger LOGGER = Logger.getLogger(AppController.class);
	private final AppService appService;

	public AppController(AppService appService) {
		this.appService = appService;		
	}
	
	/**
	 * Gets the ovd content
	 * 
	 * @param tmsId
	 * @return
	 */
	@RequestMapping(value = "/apps/{id}", method = RequestMethod.GET)
	public ResponseEntity<App> getAppById(@PathVariable Long id)  {
		App app =  appService.getAppById(id);
		if (app == null) {
			return new ResponseEntity<App>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<App>(app, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/apps/genre/{genres}", method = RequestMethod.GET)
	public ResponseEntity<Page<?>> getOvdContentByTmsId(@PathVariable String genres, Pageable pageable)  {
		Page<?> content =  appService.findApps(null,  null, genres, pageable);
		//Page<?> content = ovdContentService.findByGenre("MOVIE, SERIES", genre, pageable);
		if (content == null) {
			return new ResponseEntity<Page<?>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Page<?>>(content, HttpStatus.OK);
	}
	
	

	
}
