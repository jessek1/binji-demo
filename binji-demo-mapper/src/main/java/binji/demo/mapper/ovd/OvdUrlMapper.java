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
import binji.demo.data.entities.ovd.OvdUrlEntity;

/**
 * @author jesse keane
 *
 */
@Mapper
public interface OvdUrlMapper {

	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdUrl {}
	
	@ToOvdUrl
	OvdUrl mapToDto(OvdUrlEntity entity);
	
	OvdUrlEntity mapToEntity(OvdUrl dto);
	
	@IterableMapping(qualifiedBy = ToOvdUrl.class)
	List<OvdUrl> mapToDTOList(List<OvdUrlEntity> entity);
}
