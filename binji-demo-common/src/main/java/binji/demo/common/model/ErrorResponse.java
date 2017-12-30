package binji.demo.common.model;



/**
 * @author jesse keane
 *
 * @param <T>
 */
public class ErrorResponse<T> extends Response<T> {
	 
	protected String code;
    protected String requestId;
    protected String resource;
    //protected HashMap<String, List<String>> errors;
    
    public ErrorResponse() {
    	super(false);
    }
    
    public ErrorResponse(String code, String requestId, String resource) {
    	super(false);
    	this.code = code;
    	this.requestId = requestId;
    	this.resource = resource;
    }
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	
    
}
