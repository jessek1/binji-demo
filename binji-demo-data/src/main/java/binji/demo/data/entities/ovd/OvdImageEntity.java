package binji.demo.data.entities.ovd;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "ovd_image")
@SequenceGenerator(name="id_gen", sequenceName="ovd_image_seq")
public class OvdImageEntity extends BaseEntity {
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ovd_content_id", referencedColumnName = "id")
	private OvdContentEntity content;
	
	@Basic
	@Column(name = "thumb")
	private String thumb;
	
	@Basic
	@Column(name = "detail")
	private String detail;
	
	@Basic
	@Column(name = "bg_banner")
	private String bgBanner;
	
}
