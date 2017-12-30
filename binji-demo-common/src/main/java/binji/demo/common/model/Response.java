package binji.demo.common.model;



/**
 * @author jesse keane
 *
 * @param <T>
 */
public class Response<T> {
	
	protected T result;
	protected boolean success;
	protected int httpStatusCode;
	protected String message;
	
	public Response() {}
	
	public Response(boolean success) {
		this.success = success;
	}
	
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
		
	
}
