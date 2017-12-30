package binji.demo.core.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import binji.demo.common.model.Response;

/**
 * @author jesse keane
 *
 */
public interface D360HttpClient {
	
	<TResponse> Response<TResponse> doGet(URI uri, Class<TResponse> responseClass) throws IOException;
	<TResponse> Response<TResponse> doGet(URI uri, Class<TResponse> responseClass, Class<?>... typeParameterClasses) throws IOException;
	
	
	<TRequest, TResponse> Response<TResponse> doPost(URI uri, TRequest o, Class<TResponse> responseClass) throws IOException;
	<TRequest, TResponse> Response<TResponse> doPost(URI uri, TRequest o, Class<TResponse> responseClass, 
			Class<?>... typeParameterClasses) throws IOException;
	<TRequest, TResponse> Response<TResponse> doPost(URI uri, TRequest o, Map<String, String> headers, 
			Class<TResponse> responseClass, Class<?>... typeParameterClasses) throws IOException;
	
	<TRequest, TResponse> Response<TResponse> doPut(URI uri, TRequest o, Class<TResponse> responseClass) throws IOException;
	
	<TResponse> Response<TResponse> doDelete(URI uri, Class<TResponse> responseClass) throws IOException;
	
	<TRequest, TResponse> Response<TResponse> doDelete(URI uri, TRequest o, Class<TResponse> responseClass) throws IOException;
	
	<TResponse> Response<TResponse> doUploadPost(URI uri, InputStream inputStream, Map<String, String> headers, Class<TResponse> responseClass) throws IOException;
	
}
