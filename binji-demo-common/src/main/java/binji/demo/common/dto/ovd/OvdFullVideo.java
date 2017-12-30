package binji.demo.common.dto.ovd;

import java.time.Instant;
import java.util.Map;
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
public class OvdFullVideo extends BaseEntityDTO {
	private String videoId;
	private OvdProvider ovdProvider;
	private String studioName;
	private Set<OvdUrl> urls;
	private Integer duration;
	private Instant availableFromDateTime;
	private Instant expiresAtDateTime;
	private Set<OvdViewingOption> viewingOptions;
	private Map<String, String> additionalData;	
	private String airTimeForSort;
}
