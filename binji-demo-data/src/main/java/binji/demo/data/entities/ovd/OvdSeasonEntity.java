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
@Table(name = "ovd_seasons")
@SequenceGenerator(name="id_gen", sequenceName="ovd_seasons_seq")
public class OvdSeasonEntity extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "content_id", referencedColumnName = "id")
	private OvdContentEntity series;
	
	@Basic
	@Column(name = "seasonId")
	private Long seasonId;
	
	@Basic
	@Column(name = "seasonNumber")
	private Long seasonNumber;
}
