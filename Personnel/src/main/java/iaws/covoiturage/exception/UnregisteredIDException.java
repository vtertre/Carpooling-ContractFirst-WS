package iaws.covoiturage.exception;

public class UnregisteredIDException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnregisteredIDException(String message) {
		super(message);
	}

}
