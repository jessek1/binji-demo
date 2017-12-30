package binji.demo.mapper.ovd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import binji.demo.common.dto.ovd.OvdSeason;
import binji.demo.data.entities.ovd.OvdSeasonEntity;

/**
 * @author jesse keane
 *
 */
@Mapper
public interface OvdSeasonMapper {

	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdSeason {}
	
	@ToOvdSeason
	OvdSeason mapToDto(OvdSeasonEntity entity);
	
	OvdSeasonEntity mapToEntity(OvdSeason dto);
	
	@IterableMapping(qualifiedBy = ToOvdSeason.class)
	List<OvdSeason> mapToDTOList(List<OvdSeasonEntity> entity);
}
