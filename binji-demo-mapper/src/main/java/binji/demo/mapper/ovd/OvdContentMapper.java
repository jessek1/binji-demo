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

import binji.demo.common.dto.ovd.OvdContentEpisode;
import binji.demo.common.dto.ovd.OvdContentMovie;
import binji.demo.common.dto.ovd.OvdContentSeries;
import binji.demo.common.dto.ovd.OvdContentSpecial;
import binji.demo.common.dto.ovd.OvdContentSportsEvent;
import binji.demo.data.entities.ovd.OvdContentEntity;
import binji.demo.mapper.ovd.OvdFullVideoMapper.ToOvdFullVideo;
import binji.demo.mapper.ovd.OvdImageMapper.ToOvdImage;
import binji.demo.mapper.ovd.OvdProviderMapper.ToOvdProvider;
import binji.demo.mapper.ovd.OvdSeasonMapper.ToOvdSeason;

/**
 * @author jesse keane
 *
 */
@Mapper(uses={OvdFullVideoMapper.class,
		OvdSeasonMapper.class,
		OvdImageMapper.class})
public interface OvdContentMapper {

	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdMovie {}
	
	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdSpecial {}
	
	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdSportsEvent {}
	
	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdSeries {}
	
	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdEpisode {}
	
	
	@ToOvdMovie
    @Mappings({    	
    	@Mapping(target = "videos", qualifiedBy = ToOvdFullVideo.class),  
    	@Mapping(target = "image", qualifiedBy = ToOvdImage.class)
    })
	OvdContentMovie mapToDtoMovie(OvdContentEntity entity);
    
	@ToOvdSpecial
    @Mappings({    	
    	@Mapping(target = "videos", qualifiedBy = ToOvdFullVideo.class),  
    	@Mapping(target = "image", qualifiedBy = ToOvdImage.class)    	
    })
	OvdContentSpecial mapToDtoSpecial(OvdContentEntity entity);
	
	@ToOvdSportsEvent
    @Mappings({    	
    	@Mapping(target = "videos", qualifiedBy = ToOvdFullVideo.class),  
    	@Mapping(target = "image", qualifiedBy = ToOvdImage.class)   	
    })
	OvdContentSportsEvent mapToDtoSportsEvent(OvdContentEntity entity);
  
	@ToOvdSeries
    @Mappings({    	
    	@Mapping(target = "seasons", qualifiedBy = ToOvdSeason.class),
    	@Mapping(target = "episodes", qualifiedBy = ToOvdEpisode.class),  
    	@Mapping(target = "image", qualifiedBy = ToOvdImage.class)
    })
	OvdContentSeries mapToDtoSeries(OvdContentEntity entity);
	
	@ToOvdEpisode
    @Mappings({    	
    	@Mapping(target = "videos", qualifiedBy = ToOvdFullVideo.class),  
    	@Mapping(target = "image", qualifiedBy = ToOvdImage.class)    	
    })
	OvdContentEpisode mapToDtoEpisode(OvdContentEntity entity);
    
	
	
	
	
	@Mappings( {
		@Mapping(target="created", ignore=true),
		@Mapping(target = "updated", ignore=true)
	} )
	OvdContentEntity mapToEntity(OvdContentEpisode dto);
	

	@Mappings( {
		@Mapping(target="created", ignore=true),
		@Mapping(target = "updated", ignore=true),	
	} )
	OvdContentEntity mapToEntity(OvdContentMovie dto);
	
	@Mappings( {
		@Mapping(target="created", ignore=true),
		@Mapping(target = "updated", ignore=true),	
	} )
	OvdContentEntity mapToEntity(OvdContentSeries dto);
	
	@Mappings( {
		@Mapping(target="created", ignore=true),
		@Mapping(target = "updated", ignore=true),	
	} )
	OvdContentEntity mapToEntity(OvdContentSpecial dto);
	
	@Mappings( {
		@Mapping(target="created", ignore=true),
		@Mapping(target = "updated", ignore=true),	
	} )
	OvdContentEntity mapToEntity(OvdContentSportsEvent dto);

	
	@IterableMapping(qualifiedBy = ToOvdProvider.class)
	List<OvdContentMovie> mapToDTOListMovie(List<OvdContentEntity> entity);
	
	@IterableMapping(qualifiedBy = ToOvdProvider.class)
	List<OvdContentSpecial> mapToDTOListSpecial(List<OvdContentEntity> entity);
	
	@IterableMapping(qualifiedBy = ToOvdProvider.class)
	List<OvdContentSportsEvent> mapToDTOListSportsEvent(List<OvdContentEntity> entity);
	
	@IterableMapping(qualifiedBy = ToOvdProvider.class)
	List<OvdContentSeries> mapToDTOListSeries(List<OvdContentEntity> entity);
	
	@IterableMapping(qualifiedBy = ToOvdProvider.class)
	List<OvdContentEpisode> mapToDTOListEpisode(List<OvdContentEntity> entity);
}
