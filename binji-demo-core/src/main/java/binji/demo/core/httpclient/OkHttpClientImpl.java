package binji.demo.core.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import org.apache.log4j.Logger;

import binji.demo.common.model.ErrorResponse;
import binji.demo.common.model.Response;
import binji.demo.core.serializers.Serializer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

/**
 * @author jesse keane
 *
 */
public class OkHttpClientImpl implements D360HttpClient {
	private static Logger LOGGER = Logger.getLogger(OkHttpClient.class);	
	public static final MediaType JSON = MediaType.parse("application/json");
	public static final MediaType OCTET_STREAM = MediaType.parse("binary/octet-stream");
	private final Serializer serializer;
	private final OkHttpClient client;
	

	public OkHttpClientImpl(Serializer serializer,
			OkHttpClient client) {
		this.serializer = serializer;
		this.client = client;
	}
	
	
	
	@Override
	public <TResponse> Response<TResponse> doGet(URI uri, Class<TResponse> responseClass) throws IOException {		
		return doGet(uri, responseClass);
	}
	
	@Override
	public <TResponse> Response<TResponse> doGet(URI uri, Class<TResponse> responseClass,
			Class<?>... typeParameterClasses) throws IOException {
		Request request = new Request.Builder()
				.url(uri.toString())
				.build();
		
		return handleResponse(request, responseClass, typeParameterClasses);
	}



	

	@Override
	public <TRequest, TResponse> Response<TResponse> doPost(URI uri, TRequest o, Class<TResponse> responseClass) throws IOException {
		return doPost(uri, o, responseClass);
	}
	
	@Override
	public <TRequest, TResponse> Response<TResponse> doPost(URI uri, TRequest o, Class<TResponse> responseClass,
			Class<?>... typeParameterClasses) throws IOException {
		return doPost(uri, o, null, responseClass, typeParameterClasses);
	}
	
	@Override
	public <TRequest, TResponse> Response<TResponse> doPost(URI uri, TRequest o, Map<String, String> headers, Class<TResponse> responseClass, 
			Class<?>... typeParameterClasses) throws IOException {
		//LOGGER.debug(String.format("POST %s %s", uri.toString(), serializer.writeToString(o)));
		Builder builder = new Request.Builder()
				.url(uri.toString())
				.post(RequestBody.create(JSON, serializer.writeToString(o)));
		if (headers != null) {
			headers.forEach((k,v) -> builder.header(k, v));
		}
		Request request = builder.build();
		return handleResponse(request, responseClass, typeParameterClasses);
	}
	
	

	@Override
	public <TRequest, TResponse> Response<TResponse> doPut(URI uri, TRequest o, Class<TResponse> responseClass) throws IOException {
		Request request = new Request.Builder()
				.url(uri.toString())
				.put(RequestBody.create(JSON, serializer.writeToString(o)))
				.build();							
		return handleResponse(request, responseClass);
	}

	@Override
	public <TResponse> Response<TResponse> doDelete(URI uri, Class<TResponse> responseClass) throws IOException {
		Request request = new Request.Builder()
				.url(uri.toString())
				.delete()
				.build();					
		return handleResponse(request, responseClass);
	}

	@Override
	public <TRequest, TResponse> Response<TResponse> doDelete(URI uri, TRequest o, Class<TResponse> responseClass) throws IOException {
		Request request = new Request.Builder()
				.url(uri.toString())
				.delete(RequestBody.create(JSON, serializer.writeToString(o)))
				.build();						
		return handleResponse(request, responseClass);
	}

	@Override
	public <TResponse> Response<TResponse> doUploadPost(URI uri, InputStream inputStream, Map<String, String> headers, Class<TResponse> responseClass) throws IOException {
		Builder builder = new Request.Builder()
				.url(uri.toString())
				.post(OkHttpRequestBodyUtil.create(OCTET_STREAM, inputStream));
		if (headers != null) {
			headers.forEach((k,v) -> builder.header(k, v));
		}
		Request request = builder.build();
		return handleResponse(request, responseClass);
	}
	
	private <T> Response<T> handleResponse(Request request, Class<T> clazz, Class<?>... typeParameterClasses) throws IOException {
		Response<T> responseModel = new Response<T>();
		boolean isSuccessful = false;
		int code;
		String body;
		String message;
		
		// retrieve vars we need and let client close connection from response body
		try (okhttp3.Response response = client.newCall(request).execute()) {
			message = response.message();
			body = response.body().string();
			isSuccessful = response.isSuccessful();
			code = response.code();
		}
		
		LOGGER.debug(String.format("%s %d %s", message, code, body));
		
		if (!isSuccessful) {
			try {
				responseModel = serializer.readFromString(body, ErrorResponse.class);
			} catch (Exception e) {				
				responseModel = new ErrorResponse<T>();
				responseModel.setMessage(body);
			}
		}
		
		responseModel.setSuccess(isSuccessful);
		responseModel.setHttpStatusCode(code);
		
		// if error, nothing left to set, return now
		if (!isSuccessful) {
			return responseModel;
		}

		

		// deserialize response and return
		if (body.length() > 0) {
			try {
				if (typeParameterClasses == null)
					responseModel.setResult(serializer.readFromString(body, clazz));
				else
					responseModel.setResult(serializer.readFromString(body, clazz, typeParameterClasses));
			} catch (Exception e) {
				LOGGER.error(String.format("Failed to deserialize response for : %s", body), e);
			}
		}
		
		return responseModel;
	}



	
}
