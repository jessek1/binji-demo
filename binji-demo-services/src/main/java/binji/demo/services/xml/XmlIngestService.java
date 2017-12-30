package binji.demo.services.xml;

import binji.demo.data.xml.ovd.Ovd.OnlineMovies.Movie;
import binji.demo.data.xml.ovd.Ovd.OnlineSpecials.Special;
import binji.demo.data.xml.ovd.Ovd.OnlineSports.Sports.Sport;
import binji.demo.data.xml.ovd.Ovd.OnlineTv.Series;

/**
 * @author jesse keane
 *
 */
public interface XmlIngestService {
	void ingestOvdCatalogXml(String filePath) throws Exception;
	void ingestOvdProvidersXml(String filePath) throws Exception;
	
	void ingestOvdContent(Series series);
	void ingestOvdContent(Movie movie);
	void ingestOvdContent(Special special);
	void ingestOvdContent(Sport sport);
	void ingestOvdCatalogXmlUsingSAX(String filePath) throws Exception;
}
