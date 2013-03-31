package iaws.covoiturage.exception;

/**
 * Exception pour une demande d'un utilisateur
 * non enregistre en base de donnees
 */
public class UnregisteredIDException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnregisteredIDException(String message) {
		super(message);
	}

}
