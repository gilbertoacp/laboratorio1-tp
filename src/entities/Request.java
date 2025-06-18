package entities;

import java.util.HashMap;

public class Request extends Http {
	
	private int id;
	private String URL;
	private String method;

	public Request() {
		super();
	}

	public Request(int id, String URL, String method, HashMap<String, String> headers, String body) {
		super(headers, body);
		this.URL = URL;
		this.method = method;
		this.id = id;
	}

	public Request(String URL, String method, HashMap<String, String> headers, String body) {
		super(headers, body);
		this.URL = URL;
		this.method = method;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "Request [URL=" + URL + ", method=" + method + ", headers=" + headers + ", body=" + body + "]";
	}
}
