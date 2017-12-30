package binji.demo.common.dto.ovd;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jesse keane
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OvdProvider extends OvdProviderShort {
	
	private String alphaLightImage;
	private String alphaDarkImage;
	private String solidLightImage;
	private String solidDarkImage;
}
