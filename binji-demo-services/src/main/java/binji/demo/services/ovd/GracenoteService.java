package binji.demo.services.ovd;

import java.util.List;

import binji.demo.common.model.gracenote.ProgramImage;

/**
 * @author jesse keane
 *
 */
public interface GracenoteService {
	List<ProgramImage> getProgramImages(String tmsId);

	String getImageAbsoluteUrl(String relativeUri);
}
