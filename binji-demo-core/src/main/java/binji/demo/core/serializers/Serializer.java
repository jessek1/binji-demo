package binji.demo.core.serializers;

/**
 * Serializer interface
 * 
 * @author jesse keane
 *
 */
public interface Serializer {
	
	<T> String writeToString(T value );
	
	
	/**
	 * Deserialize json string to object
	 * 
	 * @param value
	 * @param clazz
	 * @return
	 */
	<T> T readFromString(String value, Class<T> clazz);
	
	
	/**
	 * Deserialize json string to generic type object
	 * 
	 * @param json
	 * @param clazz
	 * @param typeParameterClasses
	 * @return
	 */
	<T> T readFromString(String json, Class<T> clazz, Class<?>... typeParameterClasses);
}
