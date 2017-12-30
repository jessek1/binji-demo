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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import binji.demo.common.dto.ovd.OvdContentBase;
import binji.demo.services.ovd.OvdContentService;

/**
 * @author jesse keane
 *
 */
@Controller
@EnableWebMvc
@RequestMapping("/v0.0.0")
public class OvdContentController {
	private static Logger LOGGER = Logger.getLogger(OvdContentController.class);
	private final OvdContentService ovdContentService;

	public OvdContentController(OvdContentService ovdContentService) {
		this.ovdContentService = ovdContentService;		
	}
	
	/**
	 * Gets the ovd content
	 * 
	 * @param tmsId
	 * @return
	 */
	@RequestMapping(value = "/ovdcontent/{tmsId}", method = RequestMethod.GET)
	public ResponseEntity<OvdContentBase> getOvdContentByTmsId(@PathVariable String tmsId)  {
		OvdContentBase content =  ovdContentService.getOvdContentByTmsId(tmsId);
		if (content == null) {
			return new ResponseEntity<OvdContentBase>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<OvdContentBase>(content, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/ovdcontent/genre/{genres}", method = RequestMethod.GET)
	public ResponseEntity<Page<?>> getOvdContentByGenre(@PathVariable String genres, Pageable pageable)  {
		Page<?> content =  ovdContentService.findOvdContent("movie, series, episode",
				null, null, null, null, null, genres, true, null, pageable);
		//Page<?> content = ovdContentService.findByGenre("MOVIE, SERIES", genre, pageable);
		if (content == null) {
			return new ResponseEntity<Page<?>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Page<?>>(content, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ovdcontent/provider/{name}", method = RequestMethod.GET)
	public ResponseEntity<Page<?>> getOvdContentByProvider(@PathVariable String name, @RequestParam(required = false)String genres, Pageable pageable)  {
		Page<?> content =  ovdContentService.findOvdContent("movie, series, episode",
				null, null, null, null, null, genres, true, name, pageable);
		//Page<?> content = ovdContentService.findByGenre("MOVIE, SERIES", genre, pageable);
		if (content == null) {
			return new ResponseEntity<Page<?>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Page<?>>(content, HttpStatus.OK);
	}
	
	
	
	

	
}
