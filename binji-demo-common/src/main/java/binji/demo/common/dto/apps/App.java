package binji.demo.common.dto.apps;

import binji.demo.common.dto.BaseEntityDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jesse keane
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class App extends BaseEntityDTO {

	private String title;
	private String description;
	private String url;
	private String genres;
	private String iconImg;
	private Integer sortOrder;
	
}
