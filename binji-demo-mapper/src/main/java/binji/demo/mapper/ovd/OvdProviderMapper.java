package binji.demo.mapper.ovd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import binji.demo.common.dto.ovd.OvdProvider;
import binji.demo.common.dto.ovd.OvdProviderShort;
import binji.demo.data.entities.ovd.OvdProviderEntity;

/**
 * @author jesse keane
 *
 */
@Mapper
public interface OvdProviderMapper {

	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdProvider {}
	
	@Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToOvdProviderShort {}
	
	@ToOvdProvider  
	OvdProvider mapToDto(OvdProviderEntity entity);
	
	@ToOvdProviderShort
	OvdProviderShort mapToDtoShort(OvdProviderEntity entity);
	
	OvdProviderEntity mapToEntity(OvdProvider dto);
	
	@IterableMapping(qualifiedBy = ToOvdProvider.class)
	List<OvdProvider> mapToDtoList(List<OvdProviderEntity> entity);
}
