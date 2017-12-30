package binji.demo.services.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import binji.demo.common.dto.ovd.OvdContentBase;
import binji.demo.common.dto.ovd.OvdContentEpisode;
import binji.demo.common.dto.ovd.OvdContentMovie;
import binji.demo.common.dto.ovd.OvdContentSeries;
import binji.demo.common.dto.ovd.OvdContentSpecial;
import binji.demo.common.dto.ovd.OvdContentSportsEvent;
import binji.demo.common.dto.ovd.OvdContentType;
import binji.demo.common.dto.ovd.OvdFullVideo;
import binji.demo.common.dto.ovd.OvdProvider;
import binji.demo.common.dto.ovd.OvdSeason;
import binji.demo.common.dto.ovd.OvdUrl;
import binji.demo.common.dto.ovd.OvdViewingOption;
import binji.demo.core.serializers.Serializer;
import binji.demo.data.xml.ovd.FullVideo;
import binji.demo.data.xml.ovd.Ovd;
import binji.demo.data.xml.ovd.Ovd.OnlineMovies.Movie;
import binji.demo.data.xml.ovd.Ovd.OnlineSpecials.Special;
import binji.demo.data.xml.ovd.Ovd.OnlineSports.Sports;
import binji.demo.data.xml.ovd.Ovd.OnlineSports.Sports.Sport;
import binji.demo.data.xml.ovd.Ovd.OnlineTv.Series;
import binji.demo.data.xml.ovd.Ovd.OnlineTv.Series.Episodes.Episode;
import binji.demo.data.xml.ovd.Ovd.OnlineTv.Series.Seasons.Season;
import binji.demo.services.ovd.OvdContentService;
import binji.demo.services.ovd.OvdProviderService;

/**
 * @author jesse keane
 *
 */
public class XmlIngestServiceImpl implements XmlIngestService {

	private static Logger LOGGER = Logger.getLogger(XmlIngestServiceImpl.class);

	private final Jaxb2Marshaller jax;
	private final OvdContentService ovdService;
	private final OvdProviderService ovdProviderService;
	private final Serializer serializer;
	private final JAXBContext jaxContext;
	private OvdContentBase lastContent;

	public XmlIngestServiceImpl(final Jaxb2Marshaller jax, final OvdContentService ovdService,
			final OvdProviderService ovdProviderService, final Serializer serializer, final JAXBContext jaxContext) {
		this.jax = jax;
		this.ovdService = ovdService;
		this.ovdProviderService = ovdProviderService;
		this.serializer = serializer;
		this.jaxContext = jaxContext;
	}

	@Override
	public void ingestOvdCatalogXmlUsingSAX(String filePath) throws Exception {
		InputStream inputStream = new FileInputStream(new File(filePath));

		// create xml event reader for input stream
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

		// initialize jaxb
		Unmarshaller unmarshaller = jaxContext.createUnmarshaller();

		XMLEvent e = null;

		lastContent = ovdService.getLastAddedOvdContent();
		boolean startProcessing = false; // flag to indicate logic to start
											// trying to import content
											// used to speed up retries if
											// previous imports failed.

		// loop though the xml stream
		while ((e = xmlEventReader.peek()) != null) {

			// check the event is a Document start element
			if (e.isStartElement()) {
				String localName = ((StartElement) e).getName().getLocalPart();
				if (localName.equals("tmsId")) {
					e = xmlEventReader.nextEvent();
					e = xmlEventReader.nextEvent();
					String tmsId = ((Characters) e).getData();
					if (tmsId.equals(lastContent.getTmsId())) {
						startProcessing = true;
					} else {
						LOGGER.info(String.format("Skipping, processing not started yet...%s", tmsId));
					}
				}
				if (startProcessing || localName.equals("sport")) {
					if (localName.equals("series")) {

						Series series = unmarshaller.unmarshal(xmlEventReader, Series.class).getValue();
						ingestOvdContent(series);						
					} else if (localName.equals("movie")) {
						Movie movie = unmarshaller.unmarshal(xmlEventReader, Movie.class).getValue();
						ingestOvdContent(movie);						
					} else if (localName.equals("special")) {
						Special special = unmarshaller.unmarshal(xmlEventReader, Special.class).getValue();
						ingestOvdContent(special);						
					} else if (localName.equals("sport")) {
						Sport sport = unmarshaller.unmarshal(xmlEventReader, Sport.class).getValue();
						startProcessing = true;
						ingestOvdContent(sport);
					} else {
						xmlEventReader.next();
					}
				} else {
					xmlEventReader.next();
					
				}

			} else {
				xmlEventReader.next();
			}

		}

		LOGGER.info("ovd catalog xml import using sax completed.");
	}

	@Override
	public void ingestOvdCatalogXml(String filePath) throws Exception {
		if (filePath == null || filePath == "")
			throw new RuntimeException("File path is missing. filepath= " + filePath);

		File file = new File(filePath);
		Ovd ovd;

		ovd = (Ovd) jax.unmarshal(new StreamSource(new FileInputStream(file)));

		List<Series> series = ovd.getOnlineTv().getSeries();
		series.forEach(s -> ingestOvdContent(s));

		List<Movie> movies = ovd.getOnlineMovies().getMovie();
		movies.forEach(m -> ingestOvdContent(m));

		List<Special> specials = ovd.getOnlineSpecials().getSpecial();
		specials.forEach(s -> ingestOvdContent(s));

		List<Sports> sports = ovd.getOnlineSports().getSports();
		sports.forEach(s -> {
			s.getSport().forEach(sp -> ingestOvdContent(sp));
		});

		LOGGER.info("ovd catalog xml import completed.");
	}

	private OvdSeason mapToSeason(Season s) {
		OvdSeason season = new OvdSeason();
		if (s.getSeasonId() != null)
			season.setSeasonId(s.getSeasonId().longValue());
		if (s.getSeasonNumber() != null)
			season.setSeasonNumber(s.getSeasonNumber().longValue());
		return season;
	}

	private OvdContentEpisode mapToEpisode(Series s, Episode e) {
		OvdContentEpisode ep = new OvdContentEpisode();
		if (s.getAirDate() != null)
			ep.setAirDate(s.getAirDate().toGregorianCalendar().toInstant());
		ep.setContentType(OvdContentType.EPISODE);
		ep.setDescription(e.getDescription());
		if (e.getEpisodeNumber() != null)
			ep.setEpisodeNumber(e.getEpisodeNumber().longValue());
		ep.setGenre(e.getGenre());
		ep.setRating(e.getRating());
		if (e.getRootId() != null)
			ep.setRootId(e.getRootId().longValue());
		if (e.getSeasonNumber() != null)
			ep.setSeasonNumber(e.getSeasonNumber().longValue());
		ep.setTitle(e.getTitle());
		ep.setTmsId(e.getTmsId());

		if (e.getVideos() != null) {
			Set<OvdFullVideo> videos = e.getVideos().getVideo().stream().map(this::mapToFullVideo)
					.collect(Collectors.toSet());
			ep.setVideos(videos);
		}
		if (ovdService.getOvdContentByTmsId(ep.getTmsId()) != null) {
			LOGGER.info(String.format("Found episode, skipping.... episode: %s", ep.getTitle()));
		} else {
			// ovdService.addOvdContent(ep);
			LOGGER.info(String.format("Added episode: %s", ep.getTitle()));
		}
		return ep;
	}

	private OvdFullVideo mapToFullVideo(FullVideo v) {
		OvdFullVideo vid = new OvdFullVideo();
		if (v.getAdditionalData() != null)
			vid.setAdditionalData(v.getAdditionalData().getDatum().stream().collect(Collectors.toMap(
					d -> ((Element) d.getKey()).getTextContent(), d -> ((Element) d.getValue()).getTextContent())));
		if (v.getAvailableFromDateTime() != null)
			vid.setAvailableFromDateTime(v.getAvailableFromDateTime().toGregorianCalendar().toInstant());
		if (v.getDuration() != null)
			vid.setDuration(v.getDuration().getValue().intValue());
		if (v.getExpiresAtDateTime() != null)
			vid.setExpiresAtDateTime(v.getExpiresAtDateTime().toGregorianCalendar().toInstant());

		if (v.getHost() != null) {
			OvdProvider ovdProvider = ovdProviderService
					.getOvdProviderByProviderIdAndName(v.getHost().getId().intValue(), v.getHost().getValue());
			vid.setOvdProvider(ovdProvider);
		}
		vid.setStudioName(v.getStudioName());

		if (v.getUrls() != null) {
			Set<OvdUrl> urls = v.getUrls().getUrl().stream().map(u -> {
				OvdUrl url = new OvdUrl();
				url.setType(u.getType());
				url.setValue(u.getValue());
				return url;
			}).collect(Collectors.toSet());
			vid.setUrls(urls);
		}
		vid.setVideoId(v.getId());

		if (v.getViewingOptions() != null) {
			Set<OvdViewingOption> options = v.getViewingOptions().getViewingOption().stream().map(opt -> {
				OvdViewingOption option = new OvdViewingOption();
				if (opt.getPrice() != null)
					option.setCurrency(opt.getPrice().getCurrency());
				option.setLicense(opt.getLicense());
				if (opt.getPrice() != null)
					option.setPrice(opt.getPrice().getValue());
				if (opt.getVideo() != null)
					option.setQuality(
							String.format("%s,%s", opt.getVideo().getQuality(), opt.getVideo().getResolution()));
				return option;
			}).collect(Collectors.toSet());
			vid.setViewingOptions(options);
		}

		return vid;
	}

	@Override
	public void ingestOvdContent(Series s) {
		OvdContentSeries show = new OvdContentSeries();
		if (s.getAirDate() != null)
			show.setAirDate(s.getAirDate().toGregorianCalendar().toInstant());
		show.setContentType(OvdContentType.SERIES);
		show.setDescription(s.getDescription());

		if (s.getEpisodes() != null) {
			Set<OvdContentEpisode> episodes = s.getEpisodes().getEpisode().stream().map(e -> {
				return mapToEpisode(s, e);
			}).collect(Collectors.toSet());
			show.setEpisodes(episodes);
		}
		if (s.getRootId() != null)
			show.setRootId(s.getRootId().longValue());

		if (s.getSeasons() != null) {
			Set<OvdSeason> seasons = s.getSeasons().getSeason().stream().map(this::mapToSeason)
					.collect(Collectors.toSet());
			show.setSeasons(seasons);
		}
		show.setSeriesId(s.getSeriesId());
		show.setTitle(s.getTitle());
		show.setTmsId(s.getTmsId());
		if (ovdService.getOvdContentByTmsId(show.getTmsId()) != null) {
			LOGGER.info(String.format("Found show, skipping.... show: %s", show.getTitle()));
		} else {
			ovdService.addOvdContent(show);
			LOGGER.info(String.format("Added tv show: %s", show.getTitle()));
		}
	}

	@Override
	public void ingestOvdContent(Movie m) {
		OvdContentMovie movie = new OvdContentMovie();
		movie.setColorCode(m.getColorCode());
		movie.setContentType(OvdContentType.MOVIE);
		movie.setDescription(m.getDescription());
		movie.setGenre(m.getGenre());
		movie.setOriginalLanguage(m.getOriginalLanguage());
		movie.setProgramType(m.getProgramType());
		movie.setRating(m.getRating());
		if (m.getRootId() != null)
			movie.setRootId(m.getRootId().longValue());
		if (m.getRunTime() != null)
			movie.setRunTime(m.getRunTime().longValue());
		movie.setTitle(m.getTitle());
		movie.setTmsId(m.getTmsId());

		if (m.getVideos() != null) {
			Set<OvdFullVideo> videos = m.getVideos().getVideo().stream().map(this::mapToFullVideo)
					.collect(Collectors.toSet());
			movie.setVideos(videos);
		}
		if (m.getYear() != null)
			movie.setYear(m.getYear().getYear());
		if (ovdService.getOvdContentByTmsId(movie.getTmsId()) != null) {
			LOGGER.info(String.format("Found movie, skipping.... movie: %s", movie.getTitle()));
		} else {
			ovdService.addOvdContent(movie);
			LOGGER.info(String.format("Added movie: %s", movie.getTitle()));
		}
	}

	@Override
	public void ingestOvdContent(Special s) {
		OvdContentSpecial special = new OvdContentSpecial();
		if (s.getAirDate() != null)
			special.setAirDate(s.getAirDate().toGregorianCalendar().toInstant());
		special.setContentType(OvdContentType.SPECIAL);
		special.setDescription(s.getDescription());
		special.setGenre(s.getGenre());
		special.setRating(s.getRating());
		if (s.getRootId() != null)
			special.setRootId(s.getRootId().longValue());
		special.setTitle(s.getTitle());
		special.setTmsId(s.getTmsId());
		if (s.getVideos() != null) {
			Set<OvdFullVideo> videos = s.getVideos().getVideo().stream().map(this::mapToFullVideo)
					.collect(Collectors.toSet());
			special.setVideos(videos);
		}
		if (ovdService.getOvdContentByTmsId(special.getTmsId()) != null) {
			LOGGER.info(String.format("Found special, skipping.... special: %s", special.getTitle()));
		} else {
			ovdService.addOvdContent(special);
			LOGGER.info(String.format("Added special: %s", special.getTitle()));
		}
	}

	@Override
	public void ingestOvdContent(Sport sp) {
		sp.getSportsEvent().forEach(ev -> {
			OvdContentSportsEvent spe = new OvdContentSportsEvent();
			if (ev.getAirDate() != null)
				spe.setAirDate(ev.getAirDate().toGregorianCalendar().toInstant());
			spe.setContentType(OvdContentType.SPORT);
			spe.setDescription(ev.getDescription());
			spe.setRating(ev.getRating());
			if (ev.getRootId() != null)
				spe.setRootId(ev.getRootId().longValue());
			spe.setTitle(ev.getTitle());
			spe.setTmsId(ev.getTmsId());
			if (ev.getVideos() != null) {
				Set<OvdFullVideo> videos = ev.getVideos().getVideo().stream().map(this::mapToFullVideo)
						.collect(Collectors.toSet());
				spe.setVideos(videos);
			}
			if (ovdService.getOvdContentByTmsId(spe.getTmsId()) != null) {
				LOGGER.info(String.format("Found sports event, skipping.... sports event: %s", spe.getTitle()));
			} else {
				ovdService.addOvdContent(spe);
				LOGGER.info(String.format("Added sports event: %s", spe.getTitle()));
			}
		});
	}

	@Override
	public void ingestOvdProvidersXml(String filePath) throws Exception {
		if (filePath == null || filePath == "")
			throw new RuntimeException("File path is missing. filepath= " + filePath);

		File file = new File(filePath);

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document document = (Document) docBuilder.parse(file);

		NodeList nodes = document.getElementsByTagName("host");
		for (int x = 0; x < nodes.getLength(); x++) {
			Element el = (Element) nodes.item(x);

			OvdProvider provider = new OvdProvider();
			provider.setProviderId(Integer.parseInt(el.getElementsByTagName("id").item(0).getTextContent()));
			provider.setName(el.getElementsByTagName("name").item(0).getTextContent());
			NodeList imgNodes = el.getElementsByTagName("image");

			for (int y = 0; y < imgNodes.getLength(); y++) {
				Element imgEl = (Element) imgNodes.item(y);
				if (imgEl.getAttribute("type").equals("alpha_light")) {
					provider.setAlphaLightImage(imgEl.getTextContent());
				} else if (imgEl.getAttribute("type").equals("alpha_dark")) {
					provider.setAlphaDarkImage(imgEl.getTextContent());
				} else if (imgEl.getAttribute("type").equals("solid_light")) {
					provider.setSolidLightImage(imgEl.getTextContent());
				} else if (imgEl.getAttribute("type").equals("solid_dark")) {
					provider.setSolidDarkImage(imgEl.getTextContent());
				}
			}

			OvdProvider check = ovdProviderService.getOvdProviderByProviderIdAndName(provider.getProviderId(),
					provider.getName());
			if (check != null) {
				LOGGER.error(String.format("Provider already found so skipping the adding of it. provider= %s",
						serializer.writeToString(provider)));
				continue;
			}
			ovdProviderService.addOvdProvider(provider);
			LOGGER.info(String.format("Added ovd provider: %s", provider.getName()));
		}
	}

}
