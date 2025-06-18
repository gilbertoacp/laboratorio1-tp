package entities;

import java.util.HashMap;

public class Response extends Http {

	private int code;
	
	public Response() {
		super();
	}
	
	public Response(HashMap<String, String> headers, String body, int code) {
		super(headers, body);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Response [code=" + code + ", headers=" + headers + ", body=" + body + ", getCode()=" + getCode()
				+ ", getHeaders()=" + getHeaders() + ", getBody()=" + getBody() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
