package binji.demo.services.ovd;

import binji.demo.common.dto.ovd.OvdContentType;
import binji.demo.common.dto.ovd.OvdImage;

/**
 * @author jesse keane
 *
 */
public interface OvdImageService {
	
public static final String CACHE_NAME = "image";
	
	OvdImage getOvdImageById(Long id);
	
	OvdImage getOvdImageFromGracenote(String tmsId, OvdContentType contentType) ;
	
/*	OvdImage addOvdImage(OvdImage ovdImage);
	
	OvdImage updateOvdImage(OvdImage ovdImage);
	
	void removeOvdImage(OvdImage ovdImage);*/

}
