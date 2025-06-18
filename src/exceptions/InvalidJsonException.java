package exceptions;

public class InvalidJsonException extends Exception {

	public InvalidJsonException() {
	}

	public InvalidJsonException(String s) {
	    super(s);
	}

	public InvalidJsonException(String s, Throwable throwable) {
	    super(s, throwable);
	}

	public InvalidJsonException(Throwable throwable) {
		super(throwable);
	}
}
