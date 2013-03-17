package iaws.covoiturage.services;

import org.w3c.dom.Element;

import iaws.covoiturage.domain.nomenclature.FirstName;
import iaws.covoiturage.domain.nomenclature.LastName;
import iaws.covoiturage.domain.nomenclature.Mail;
import iaws.covoiturage.domain.nomenclature.MailingAddress;

public interface InscriptionService {

	/**
	 * Tente d'enregistrer un nouvel enseignant
	 * et retourne une chaine de caractères représentant
	 * un message suivant :
	 * "OK" si l'enregistrement est réussi
	 * "ERREUR_100" si l'adresse mail est déjà utilisée
	 * "ERREUR_110" si l'adresse mail est invalide
	 * "ERREUR_200" si l'adresse postale n'est pas connue d'OSM
	 * 
	 * @param lastName le nom de famille
	 * @param firstName le prénom
	 * @param mail l'adresse mail
	 * @param mailingAddress l'adresse postale
	 * @return
	 */
	public Element postTeacher(LastName lastName, FirstName firstName,
			Mail mail, MailingAddress mailingAddress) throws Exception;
	
}
