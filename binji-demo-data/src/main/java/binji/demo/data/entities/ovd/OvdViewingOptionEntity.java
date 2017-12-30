package binji.demo.data.entities.ovd;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ovd_viewing_options")
@SequenceGenerator(name="id_gen", sequenceName="ovd_viewing_options_seq")
public class OvdViewingOptionEntity extends BaseEntity {

	@Basic
	@Column(name = "license")
	private String license;
	
	@Basic
	@Column(name = "price")
	private BigDecimal price;
	
	@Basic
	@Column(name = "currency")
	private String currency;
	
	@Basic
	@Column(name = "quality")
	private String quality;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ovd_fullvideos_id", referencedColumnName = "id")
	private OvdFullVideoEntity fullVideo;
	
}
