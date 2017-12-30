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
public class OvdContentSpecial extends OvdContentBase {
	private Instant airDate;
	private String rating;
	private String genre;
	private Set<OvdFullVideo> videos;
}
