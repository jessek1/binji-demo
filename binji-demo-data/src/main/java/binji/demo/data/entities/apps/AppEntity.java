package binji.demo.data.entities.apps;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Table(name = "apps")
@SequenceGenerator(name="id_gen", sequenceName="apps_seq")
public class AppEntity extends BaseEntity{

	@Basic
	@Column(name = "title")
	private String title;
	
	@Basic
	@Column(name ="description", length=2083)
	private String description;
	
	@Basic
	@Column(name ="url", length=2083)
	private String url;
	
	@Basic
	@Column(name ="genres", columnDefinition="TEXT")
	private String genres;
	
	@Basic
	@Column(name ="icon_img", length=2083)
	private String iconImg;
	
	@Basic
	@Column(name ="sortOrder")
	private Integer sortOrder;
	
	/*@ManyToMany	
	@JoinTable(name = "apps_genres", 
	joinColumns = @JoinColumn(name = "apps_id", referencedColumnName = "id"),
	inverseJoinColumns = @JoinColumn(name = "genres_id", referencedColumnName = "id"))	
	private Set<AppGenreEntity> genres;*/
	
	
}
