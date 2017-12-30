package binji.demo.common.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * @author jesse keane
 *
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id", doNotUseGetters = true)
public abstract class BaseEntityDTO implements Serializable {

	private static final long serialVersionUID = -276731678027836098L;
	
	private Long id;
	private Long version = 0L;
	
}
