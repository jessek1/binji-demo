package binji.demo.common.dto.ovd;

import java.math.BigDecimal;
import java.util.Set;

import binji.demo.common.dto.BaseEntityDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jesse keane
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OvdViewingOption extends BaseEntityDTO {
	private String license;
	private BigDecimal price;
	private String currency;
	private String quality;
}
