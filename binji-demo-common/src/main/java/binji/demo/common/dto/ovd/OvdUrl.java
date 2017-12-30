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
public class OvdUrl extends BaseEntityDTO {
	private String type;
	private String value;
}
