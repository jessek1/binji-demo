package binji.demo.data.entities.ovd;

import java.time.Instant;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import binji.demo.common.dto.ovd.OvdContentType;
import binji.demo.data.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jesse keane
 *
 */
@Getter
@Setter
@Entity
@Table(name = "ovd_content")
@SequenceGenerator(name="id_gen", sequenceName="ovd_content_seq")
public class OvdContentEntity extends BaseEntity{

	@Basic
	@Column(name = "root_id")
	private Long rootId;
	
	@Basic
	@Column(name ="tms_id")
	private String tmsId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "contentType")
	private OvdContentType contentType;
		
	@Column(name = "feed_timestamp", columnDefinition="timestamptz")
	private Instant feedTimestamp;
	
	@Basic
	@Column(name = "title", columnDefinition="TEXT")
	private String title;
	
	@Basic
	@Column(name = "description", columnDefinition="TEXT")
	private String description;
	
	@Basic
	@Column(name = "airDate", columnDefinition="timestamptz")
	private Instant airDate;
	
	@Basic
	@Column(name = "rating", columnDefinition="TEXT")
	private String rating;
	
	@Basic
	@Column(name = "genre", columnDefinition="TEXT")
	private String genre;
	
	@Basic
	@Column(name = "programType")
	private String programType;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "content")	
	private Set<OvdFullVideoEntity> videos;
	
	@Basic
	@Column(name = "year")
	private Integer year;
	
	@Basic
	@Column(name = "runTime")
	private Long runTime;
	
	@Basic
	@Column(name = "colorCode")
	private String colorCode;
	
	@Basic
	@Column(name = "originalLanguage")
	private String originalLanguage;
	
	@Basic
	@Column(name = "seriesId")
	private String seriesId;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "series")	
	private Set<OvdSeasonEntity> seasons;
	
	@ManyToOne
	private OvdContentEntity parent;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="parent")
	private Set<OvdContentEntity> episodes;
	
	@Basic
	@Column(name = "seasonNumber")
	private Long seasonNumber;
	
	@Basic
	@Column(name = "episodeNumber")
	private Long episodeNumber;
	
	@OneToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL, mappedBy="content")
	@JoinColumn(name = "id", referencedColumnName = "ovd_content_id", unique = true)
	private OvdImageEntity image;
	
	
	
	/**
	 * Explicit setter to maintain relationship
	 */
	public void setVideos(Set<OvdFullVideoEntity> videos) {	
		if (videos != null) {
			this.videos = videos;
			videos.forEach(x -> x.setContent(this));
		}
	}
	
	/**
	 * Explicit setter to maintain relationship
	 */
	public void setEpisodes(Set<OvdContentEntity> episodes) {	
		if (episodes != null) {
			this.episodes = episodes;
			episodes.forEach(x -> x.setParent(this));
		}
	}
	
	/**
	 * Explicit setter to maintain relationship
	 */
	public void setImage(OvdImageEntity image) {	
		if (image != null) {
			this.image = image;
			image.setContent(this);
		}
	}
}
