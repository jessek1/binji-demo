package binji.demo.data.entities.ovd;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
@Table(name = "ovd_providers")
@SequenceGenerator(name="id_gen", sequenceName="ovd_providers_seq")
public class OvdProviderEntity extends BaseEntity {
	
	@Basic
	@Column(name = "provider_id")
	private Integer providerId;
	
	@Basic
	@Column(name = "name", columnDefinition="TEXT")
	private String name;
	
	@Basic
	@Column(name = "alpha_light_image")
	private String alphaLightImage;
	
	@Basic
	@Column(name = "alpha_dark_image")
	private String alphaDarkImage;
	
	@Basic
	@Column(name = "solid_light_image")
	private String solidLightImage;
	
	@Basic
	@Column(name = "solid_dark_image")
	private String solidDarkImage;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ovdProvider")	
	private Set<OvdFullVideoEntity> fullVideos;
}
