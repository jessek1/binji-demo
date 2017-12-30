package binji.demo.services.ovd;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import binji.demo.common.model.Response;
import binji.demo.common.model.gracenote.ProgramImage;
import binji.demo.core.httpclient.gracenote.GracenoteClient;
import binji.demo.core.serializers.Serializer;

/**
 * @author jesse keane
 *
 */
public class GracenoteServiceImpl implements GracenoteService {
	
	private static Logger LOGGER = Logger.getLogger(GracenoteServiceImpl.class);
	
	private final GracenoteClient client;
	private final Serializer serializer;
	private final String baseUrl;
	private final String apiKey;
	
	public GracenoteServiceImpl(GracenoteClient client, Serializer serializer, String baseUrl, String apiKey) {
		this.client = client;
		this.serializer = serializer;
		this.baseUrl = baseUrl;
		this.apiKey = apiKey;
	}

	@Override
	public List<ProgramImage> getProgramImages(String tmsId) {
		List<ProgramImage> images = null;
		try {
			Response<List<ProgramImage>> response = client.getProgramImagesClient().getProgramImages(tmsId);
			if (!response.isSuccess()) {
				LOGGER.error(String.format("Request failed : %s", serializer.writeToString(response)));
				return null;
			}
			images = response.getResult();
			if (images == null) 
				return null;			
		} catch (IOException e) {
			throw new RuntimeException("Failed to get program images.", e);
		}	
		return images;
	}
	
	@Override
	public String getImageAbsoluteUrl(String relativeUri) {
		return String.format("%s/%s?api_key=%s", baseUrl, relativeUri, apiKey );
	}


}
