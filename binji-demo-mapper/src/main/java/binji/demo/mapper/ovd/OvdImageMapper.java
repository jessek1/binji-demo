package binji.demo.mapper.ovd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Qualifier;

import binji.demo.common.dto.ovd.OvdImage;
import binji.demo.data.entities.ovd.OvdImageEntity;

/**
 * @author jesse keane
 *
 */
@Mapper
public interface OvdImageMapper {
	
	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdImage {}

	@ToOvdImage
	@Mappings( {
		@Mapping(target="ovdContentId", source = "content.id"),		
	} )
	OvdImage mapToDto(OvdImageEntity entity);
   
	@Mappings( {
		@Mapping(target="created", ignore=true),
		@Mapping(target = "updated", ignore=true),
		@Mapping(target="content.id", source = "ovdContentId"),	
	} )
	OvdImageEntity mapToEntity(OvdImage dto);	
}
