package binji.demo.data.entities.ovd;

import javax.persistence.Basic;
import javax.persistence.Column;
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
@Table(name = "ovd_urls")
@SequenceGenerator(name="id_gen", sequenceName="ovd_urls_seq")
public class OvdUrlEntity extends BaseEntity {
	
	@Basic
	@Column(name = "type")
	private String type;
	
	@Basic
	@Column(name = "value", columnDefinition="TEXT")
	private String value;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ovd_fullvideos_id", referencedColumnName = "id")
	private OvdFullVideoEntity fullVideo;
}
