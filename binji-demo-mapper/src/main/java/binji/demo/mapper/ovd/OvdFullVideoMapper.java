package binji.demo.mapper.ovd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Qualifier;

import binji.demo.common.dto.ovd.OvdFullVideo;
import binji.demo.common.dto.ovd.OvdProvider;
import binji.demo.data.entities.ovd.OvdFullVideoEntity;
import binji.demo.data.entities.ovd.OvdProviderEntity;
import binji.demo.mapper.ovd.OvdProviderMapper.ToOvdProviderShort;
import binji.demo.mapper.ovd.OvdUrlMapper.ToOvdUrl;
import binji.demo.mapper.ovd.OvdViewingOptionMapper.ToOvdViewingOption;

/**
 * @author jesse keane
 *
 */
@Mapper(uses={ OvdProviderMapper.class,
		OvdUrlMapper.class,
		OvdViewingOptionMapper.class})
public interface OvdFullVideoMapper {

	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdFullVideo {}
	
	@ToOvdFullVideo
    @Mappings({    	
    	@Mapping(target = "ovdProvider", qualifiedBy = ToOvdProviderShort.class),
    	@Mapping(target = "urls", qualifiedBy = ToOvdUrl.class),
    	@Mapping(target = "viewingOptions", qualifiedBy = ToOvdViewingOption.class),
    })
	OvdFullVideo mapToDto(OvdFullVideoEntity entity);
	
	@Mappings( {
		@Mapping(target="created", ignore=true),
		@Mapping(target = "updated", ignore=true),	
	} )
	OvdFullVideoEntity mapToEntity(OvdFullVideo dto);
	
	
	
	
	@IterableMapping(qualifiedBy = ToOvdFullVideo.class)
	List<OvdProvider> mapToDTOList(List<OvdProviderEntity> entity);
}
