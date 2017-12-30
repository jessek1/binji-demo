
package binji.demo.core.serializers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * Jackson serializer 
 * 
 * @author jesse keane
 *
 */
public class JacksonSerializer implements Serializer{
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	public JacksonSerializer() {
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public <T> String writeToString(T value) {
		if (value == null) {
			return null;
		}
		try {
			return mapper.writeValueAsString(value);
		} catch (Exception ex) {
			throw new RuntimeException("Error serializing entity ", ex);
		}	
	}

	public <T> T readFromString(String json, Class<T> clazz) {
		try {
			if(clazz.getTypeParameters().length > 0) {
				return mapper.readValue(json, new TypeReference<T>(){});
			}
			
			return mapper.readValue(json, clazz);
		} catch (Exception ex) {
			throw new RuntimeException("Error deserializing entity ", ex);
		}
	}	
	
	public <T> T readFromString(String json, Class<T> clazz, Class<?>... typeParameterClasses) {
		try {			
			JavaType type = mapper.getTypeFactory().constructParametricType(
					clazz, typeParameterClasses);
			return mapper.readValue(json, type);
		} catch (Exception ex) {
			throw new RuntimeException("Error deserializing entity ", ex);
		}
	}

}
