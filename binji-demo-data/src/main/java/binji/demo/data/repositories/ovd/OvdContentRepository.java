package binji.demo.data.repositories.ovd;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import binji.demo.data.entities.ovd.OvdContentEntity;

/**
 * @author jesse keane
 *
 */
public interface OvdContentRepository extends JpaRepository<OvdContentEntity, Long> {

	OvdContentEntity findByRootId(Long rootId);

	OvdContentEntity findByTmsId(String tmsId);

	OvdContentEntity findTopByOrderByIdDesc();

	@Query(value = "select  ovdcontent0_.id as id1_1_, ovdcontent0_.created as created2_1_, ovdcontent0_.updated as updated3_1_, ovdcontent0_.version as version4_1_, ovdcontent0_.airDate as airDate5_1_, ovdcontent0_.colorCode as colorCod6_1_, ovdcontent0_.contentType as contentT7_1_, ovdcontent0_.description as descript8_1_, ovdcontent0_.episodeNumber as episodeN9_1_, ovdcontent0_.feed_timestamp as feed_ti10_1_, ovdcontent0_.genre as genre11_1_, ovdcontent0_.originalLanguage as origina12_1_, ovdcontent0_.parent_id as parent_22_1_, ovdcontent0_.programType as program13_1_, ovdcontent0_.rating as rating14_1_, ovdcontent0_.root_id as root_id15_1_, ovdcontent0_.runTime as runTime16_1_, ovdcontent0_.seasonNumber as seasonN17_1_, ovdcontent0_.seriesId as seriesI18_1_, ovdcontent0_.title as title19_1_, ovdcontent0_.tms_id as tms_id20_1_, ovdcontent0_.year as year21_1_ "
			+ "from ovd_content ovdcontent0_ "
			+ "left outer join ovd_fullvideos videos1_ on ovdcontent0_.id=videos1_.content_id "
			+ "left outer join ovd_providers ovdprovide2_ on videos1_.ovd_provider_id=ovdprovide2_.id "
			+ "where (ovdcontent0_.contentType in (?1)) and (lower(ovdcontent0_.genre) like ?2) "
			+ "and (ovdprovide2_.name not in  "
			+ "     ( "
			+ "'XFINITY', " 
			+ "'Netflix MX', " 
			+ "'YouTube MX', "
			+ "'Netflix DE', "
			+ "'Netflix UK', "
			+ "'YouTube CA', "
			+ "'YouTube UK', "
			+ "'Netflix CA', "
			+ "'iTunes CA', "
			+ "'iTunes UK', "
			+ "'ITV', "
			+ "'CBC', "
			+ "'CTV', "
			+ "'YouTube MX', "
			+ "'Netflix DE', "
			+ "'Netflix FR', "
			+ "'Netflix DK', "
			+ "'Netflix FI', "
			+ "'Netflix NO', "
			+ "'ZDF', "
			+ "'Arte', "
			+ "'Maxdome', "
			+ "'TF1', "
			+ "'FilmoTV', "
			+ "'France TV Pluzz VAD', "
			+ "'France TV Pluzz', "
			+ "'Wuaki ES', "
			+ "'6Play', "
			+ "'Chili Web', "
			+ "'ARD', "
			+ "'Amazon UK', "
			+ "'iTunes DE', "
			+ "'iTunes FR', "
			+ "'Videociety DE', "
			+ "'Netflix AT', "
			+ "'Viewster DE', "
			+ "'Viewster AT', "
			+ "'Crackle CA', "
			+ "'Netflix CH-fr', "
			+ "'Netflix CH-de', "
			+ "'Netflix BR', "
			+ "'TNT Go BR', "
			+ "'Space Go BR', "
			+ "'Telecine Play BR', "
			+ "'Crackle BR', "
			+ "'iTunes BR', "
			+ "'YouTube BR', "
			+ "'Cartoon Go BR', "
			+ "'Esporte Interativo', "
			+ "'Globosat GNT', "
			+ "'Globosat Universal', "
			+ "'Globosat SporTV', "
			+ "'Globosat Multishow', "
			+ "'Globosat BIS', "
			+ "'Globosat Viva', "
			+ "'Globosat Canal Off', "
			+ "'Globosat Gloob', "
			+ "'Globosat GloboNews', "
			+ "'Globosat Canal Brasil', "
			+ "'Globosat Megapix', "
			+ "'Globosat Combate', "
			+ "'Globosat Globosat', "
			+ "'Espn BR', "
			+ "'Philos', "
			+ "'7TV', "
			+ "'Sexy Hot', "
			+ "'Sky', "
			+ "'Netflix AU', "
			+ "'Fox Brazil Fox', "
			+ "'Fox Brazil FX', "
			+ "'Fox Brazil Sports', "
			+ "'Fox Brazil Life', "
			+ "'Fox Brazil National Geographic', "
			+ "'Netflix NZ', "
			+ "'iTunes AU', "
			+ "'Telstra', "
			+ "'SBS', "
			+ "'Stan', "
			+ "'Tenplay', "
			+ "'Wuaki FR', "
			+ "'Wuaki DE', "
			+ "'Wuaki IT', "
			+ "'Space Go MX', "
			+ "'TNT Go MX', "
			+ "'Fox Mexico Fox', "
			+ "'Fox Mexico Sports', "
			+ "'Fox Mexico FX', "
			+ "'Fox Mexico Life', "
			+ "'Fox Mexico National Geographic', "
			+ "'Fox Mexico Plus', "
			+ "'Wuaki UK', "
			+ "'PlusSeven', "
			+ "'NineNow', "
			+ "'ABCiView', "
			+ "'Globosat Syfy', "
			+ "'Fox Brazil Premium', "
			+ "'UKTV', "
			+ "'RTL TV Now', "
			+ "'HBO BR Documentaries', "
			+ "'HBO BR Kids', "
			+ "'HBO BR Movies', "
			+ "'HBO BR Series', "
			+ "'HBO BR Specials', "
			+ "'HBO BR Adult', "
			+ "'CW Seed', "
			+ "'Discovery Kids BR', "
			+ "'Foxtel Play', "
			+ "'Noggin BR', "
			+ "'AXN BR', "
			+ "'A&E BR', "
			+ "'E! BR', "
			+ "'Telemundo', "
			+ "'Sony BR', "
			+ "'History BR', "
			+ "'Lifetime BR', "
			+ "'Telekom Sport', "
			+ "'Netflix AU Telstra', "
			+ "'Fox Mexico Premium', "
			+ "'Hayu UK', "
			+ "'VM Store UK', "
			+ "'SevenPlus', "
			+ "     )) "
			+ "order by videos1_.airtime desc nulls last, ovdcontent0_.year desc nulls last #pageable ", nativeQuery = true)
	List<Object[]> findByGenre(List<String> contentTypeCsv, String genre, Pageable pageable);
}