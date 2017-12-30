package binji.demo.common.dto.ovd;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jesse keane
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OvdContentMovie extends OvdContentBase {
	private String rating;
	private String genre;
	private String programType;
	private Set<OvdFullVideo> videos;
	private Integer year;
	private Long runTime;
	private String colorCode;
	private String originalLanguage;
}
