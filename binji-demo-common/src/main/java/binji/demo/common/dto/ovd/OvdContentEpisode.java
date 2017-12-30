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
public class OvdContentEpisode extends OvdContentBase {
	private Instant airDate;
	private String rating;
	private String genre;
	private Set<OvdFullVideo> videos;
	private Long seasonNumber;
	private Long episodeNumber;
	private Long parentId = 0L;
}
