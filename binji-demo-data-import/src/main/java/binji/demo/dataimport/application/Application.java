package binji.demo.dataimport.application;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import binji.demo.core.configuration.RootConfig;
import binji.demo.dataimport.configuration.ImportConfig;
import binji.demo.services.csv.CsvIngestService;
import binji.demo.services.xml.XmlIngestService;


/**
 * @author jesse keane
 *
 */
public class Application {

	private static Logger LOGGER = Logger.getLogger(Application.class);
	
	@Autowired
	private XmlIngestService xmlIngestService;
	@Autowired
	private CsvIngestService csvIngestService;

	

	public static void main(String[] args) {
		String cmd = args[0];

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ImportConfig.class, RootConfig.class);		
		/*PropertySourcesPlaceholderConfigurer pph = new PropertySourcesPlaceholderConfigurer();
        pph.setLocation(new ClassPathResource("binji-demo.properties"));
        ctx.addBeanFactoryPostProcessor(pph);*/
		ctx.refresh();
		ctx.start();
		// XmlIngestService ingestService =
		// context.getBean(XmlIngestService.class);
		Application main = ctx.getBean(Application.class);
		try {
			if (cmd.equalsIgnoreCase("ovdcatalogimport")) {
				main.importOvdCatalog(args[1]);
			} else if (cmd.equalsIgnoreCase("ovdcatalogimportusingSax")) {
				main.importOvdCatalogUsingSAX(args[1]);
			} else if (cmd.equalsIgnoreCase("ovdprovidersimport")) {
				main.importOvdProviders(args[1]);
			} else if (cmd.equalsIgnoreCase("appsimport")) {
				main.importAppsListing(args[1]);
			} else {
				LOGGER.error("Command not recognized.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void importOvdCatalog(String filePath) throws Exception {
		xmlIngestService.ingestOvdCatalogXml(filePath);
	}
	
	public void importOvdCatalogUsingSAX(String filePath) throws Exception {
		xmlIngestService.ingestOvdCatalogXmlUsingSAX(filePath);
	}
	
	public void importOvdProviders(String filePath) throws Exception {
		xmlIngestService.ingestOvdProvidersXml(filePath);
	}
	
	public void importAppsListing(String filePath) throws Exception {
		csvIngestService.ingestAppListingTabDelimited(filePath);
	}
}
