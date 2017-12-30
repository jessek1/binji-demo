package binji.demo.mapper.apps;

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

import binji.demo.common.dto.apps.App;
import binji.demo.data.entities.apps.AppEntity;

/**
 * @author jesse keane
 *
 */
@Mapper
public interface AppMapper {
	
	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToApp {}
  
	@ToApp
	App mapToDto(AppEntity entity);
    
	@Mappings( {
		@Mapping(target="created", ignore=true),
		@Mapping(target = "updated", ignore=true)
	} )
	AppEntity mapToEntity(App dto);
	
	@IterableMapping(qualifiedBy = ToApp.class)
	List<App> mapToDtoList(List<AppEntity> entity);
}
