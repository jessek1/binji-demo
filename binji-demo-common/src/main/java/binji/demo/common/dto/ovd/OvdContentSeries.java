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
public class OvdContentSeries extends OvdContentBase {
	private String seriesId;
	private Instant airDate;
	private Set<OvdSeason> seasons;
	private Set<OvdContentEpisode> episodes;

}
