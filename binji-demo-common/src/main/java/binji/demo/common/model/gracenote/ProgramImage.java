package binji.demo.common.model.gracenote;

import lombok.Data;

/**
 * @author jesse keane
 *
 */
@Data
public class ProgramImage {
	private String uri;
	private String height;
	private String width;
	private String size;
	private String aspect;
	private String primary;
	private String category;
	private String tier;
	private String text;
	private ProgramImageCaption caption;
	private String personIds;
}
