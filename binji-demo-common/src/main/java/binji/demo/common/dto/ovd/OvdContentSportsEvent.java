package binji.demo.common.dto.ovd;

import java.time.Instant;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jesse keane
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OvdContentSportsEvent extends OvdContentBase {
	private Instant airDate;
	private String rating;
	private Set<OvdFullVideo> videos;
}
