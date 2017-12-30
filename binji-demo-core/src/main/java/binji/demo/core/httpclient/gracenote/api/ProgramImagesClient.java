package binji.demo.core.httpclient.gracenote.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.log4j.Logger;

import binji.demo.common.model.Response;
import binji.demo.common.model.gracenote.ProgramImage;
import binji.demo.core.httpclient.D360HttpClient;

/**
 * @author jesse keane
 *
 */
public class ProgramImagesClient {
	private static Logger LOGGER = Logger.getLogger(ProgramImagesClient.class);
	private final D360HttpClient httpClient;
	private final String BASE_URL;
	private final String API_KEY;
	
	public ProgramImagesClient(D360HttpClient httpClient,
			String baseUrl,
			String apiKey) {
		this.httpClient = httpClient;
		this.BASE_URL = baseUrl;
		this.API_KEY = apiKey;
	}
	
	public Response<List<ProgramImage>> getProgramImages(String tmsId) throws IOException {
		URI uri = null;
		try {
			uri = new URI(String.format("%s/programs/%s/images?api_key=%s", BASE_URL, tmsId, API_KEY));
		} catch (URISyntaxException e) {
			LOGGER.error("URI error", e);
			return null;
		}
		return  (Response<List<ProgramImage>>)(Response<?>) httpClient.doGet(uri, List.class, ProgramImage.class);
	}
}
