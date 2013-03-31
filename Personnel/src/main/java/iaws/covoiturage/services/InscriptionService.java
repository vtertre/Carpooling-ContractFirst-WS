package iaws.covoiturage.services;

import iaws.covoiturage.domain.nomenclature.FirstName;
import iaws.covoiturage.domain.nomenclature.LastName;
import iaws.covoiturage.domain.nomenclature.Mail;
import iaws.covoiturage.domain.nomenclature.MailingAddress;

public interface InscriptionService {

	/**
	 * Tente d'enregistrer un nouvel enseignant
	 * 
	 * @param lastName le nom de famille
	 * @param firstName le prenom
	 * @param mail l'adresse mail
	 * @param mailingAddress l'adresse postale
	 * @return une chaine de caracteres representant
	 * un message suivant :
	 * "OK" si l'enregistrement est reussi
	 * "ERREUR_100" si l'adresse mail est deja utilisee
	 * "ERREUR_110" si l'adresse mail est invalide
	 * "ERREUR_200" si l'adresse postale n'est pas connue d'OSM
	 */
	public String postTeacher(LastName lastName, FirstName firstName,
			Mail mail, MailingAddress mailingAddress) throws Exception;
	
}
