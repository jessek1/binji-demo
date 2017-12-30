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
public class OvdImage extends BaseEntityDTO{
	private String thumb;
	private String detail;
	private String bgBanner;
	private Long ovdContentId;
	
}
