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
public class OvdSeason extends BaseEntityDTO {
	private Long seasonId;
	private Long seasonNumber;
}
