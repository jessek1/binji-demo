package binji.demo.mapper.ovd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import binji.demo.common.dto.ovd.OvdUrl;
import binji.demo.common.dto.ovd.OvdViewingOption;
import binji.demo.data.entities.ovd.OvdUrlEntity;
import binji.demo.data.entities.ovd.OvdViewingOptionEntity;

/**
 * @author jesse keane
 *
 */
@Mapper
public interface OvdViewingOptionMapper {

	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdViewingOption {}
	
	@ToOvdViewingOption
	OvdViewingOption mapToDto(OvdViewingOptionEntity entity);
	
	OvdViewingOptionEntity mapToEntity(OvdViewingOption dto);
	
	@IterableMapping(qualifiedBy = ToOvdViewingOption.class)
	List<OvdViewingOption> mapToDTOList(List<OvdViewingOptionEntity> entity);
}
