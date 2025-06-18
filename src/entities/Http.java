package entities;

import java.util.HashMap;

public abstract class Http {
	
	protected HashMap<String, String> headers;
	protected String body;
	
	protected Http() {
		
	}
	
	protected Http(HashMap<String, String> headers, String body) {
		this.headers = headers;
		this.body = body;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
