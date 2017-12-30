package binji.demo.core.httpclient.gracenote;

import org.apache.log4j.Logger;

import binji.demo.core.httpclient.gracenote.api.ProgramImagesClient;

/**
 * @author jesse keane
 *
 */
public class GracenoteClient {
	private static Logger LOGGER = Logger.getLogger(GracenoteClient.class);
	private final ProgramImagesClient programImagesClient;
	
	

	public GracenoteClient(ProgramImagesClient programImagesClient) {
		this.programImagesClient = programImagesClient;
	}
		
	public ProgramImagesClient getProgramImagesClient() {
		return this.programImagesClient;
	}
	
	
}
