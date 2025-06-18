package entities;

public class History {
	
	private Request request;
    private String timestamp;
    
    public History() {
    	this.request = new Request();
    }

	public History(Request request, String timestamp) {
		this.request = request;
		this.timestamp = timestamp;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
