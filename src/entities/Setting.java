package entities;

public class Setting {

	private boolean sslVerification;
	private boolean followRedirections;
	
	public Setting() {
	}
	
	public Setting(boolean sslVerification, boolean followRedirections) {
		this.sslVerification = sslVerification;
		this.followRedirections = followRedirections;
	}

	public boolean isSslVerification() {
		return sslVerification;
	}

	public void setSslVerification(boolean sslVerification) {
		this.sslVerification = sslVerification;
	}

	public boolean isFollowRedirections() {
		return followRedirections;
	}

	public void setFollowRedirections(boolean followRedirections) {
		this.followRedirections = followRedirections;
	}
	
	@Override
	public String toString() {
		return "{\"followRedirections\":"+ this.followRedirections +", \"sslVerification\":"+ this.sslVerification +"}";
	}

}
