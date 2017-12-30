package binji.demo.services.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import binji.demo.common.dto.apps.App;
import binji.demo.services.apps.AppService;

/**
 * @author jesse keane
 *
 */
public class CsvIngestServiceImpl implements CsvIngestService {
	private static Logger LOGGER = Logger.getLogger(CsvIngestServiceImpl.class);
	
	private final AppService appService;
	
	public CsvIngestServiceImpl(final AppService appService){
		this.appService = appService;
	}

	@Override
	public void ingestAppListingTabDelimited(String filePath) {
		if (filePath == null || filePath == "")
			throw new RuntimeException("File path is missing. filepath= " + filePath);
		
		int COL_TITLE = 0;
		int COL_URL = 2;
		int COL_GENRE = 3;
		int COL_DESC = 4;
		int COL_THUMB = 5;
		

		File file = new File(filePath);
		try(InputStream inputFS = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(inputFS))) {
			String line = "";
			br.readLine();			
			while((line = br.readLine()) != null) {
				String[] fields = line.split("\t", -1);
				if (fields.length > 0) {
					App app = new App();
					app.setTitle(fields[COL_TITLE]);
					if (fields.length-1 >= COL_DESC)
						app.setDescription(fields[COL_DESC]);
					if (fields.length-1 >= COL_THUMB)
						app.setIconImg(fields[COL_THUMB]);
					if (fields.length-1 >= COL_URL)
						app.setUrl(fields[COL_URL]);
					if (fields.length-1 >= COL_GENRE)
						app.setGenres(fields[COL_GENRE]);
					
					
					if (appService.getAppByTitle(app.getTitle()) != null) {
						LOGGER.info(String.format("Skipping app, found existing already.... app: %s", app.getTitle()));
					} else {
						appService.addApp(app);
						LOGGER.info(String.format("Added app: %s", app.getTitle()));
					}
				}						
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOGGER.info("completed adding apps.");
		

	}

}
