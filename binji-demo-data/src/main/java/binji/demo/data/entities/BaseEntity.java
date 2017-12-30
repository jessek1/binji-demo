package binji.demo.data.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;


/**
 * @author jesse keane
 *
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity implements Serializable {
	
	
	private static final long serialVersionUID = 364613400408153366L;

	@Id 	
	@GeneratedValue (strategy=GenerationType.SEQUENCE, generator="id_gen") 
	@Column(name= "id", updatable = false)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Basic(optional = false)
    @Column(name = "created", nullable = false, updatable = false, columnDefinition="timestamptz")  
    @CreatedDate
    private Instant created = Instant.now();

    @Basic(optional = false)
    @Column(name = "updated", nullable = false, columnDefinition="timestamptz")
    @LastModifiedDate
    private Instant updated = Instant.now();
    
    
   
}