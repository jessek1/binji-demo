package binji.demo.common.dto.ovd;

import binji.demo.common.dto.BaseEntityDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jesse keane
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public  class OvdContentBase extends BaseEntityDTO {

	private Long rootId;
	private String tmsId;
	private String title;
	private String description;	
	private OvdContentType contentType;
	private OvdImage image;
}
