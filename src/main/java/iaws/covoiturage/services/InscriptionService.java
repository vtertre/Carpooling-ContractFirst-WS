package iaws.covoiturage.services;

import org.w3c.dom.Element;

import iaws.covoiturage.domain.nomenclature.FirstName;
import iaws.covoiturage.domain.nomenclature.LastName;
import iaws.covoiturage.domain.nomenclature.Mail;
import iaws.covoiturage.domain.nomenclature.MailingAddress;

public interface InscriptionService {

	/**
	 * Tente d'enregistrer un nouvel enseignant
	 * et retourne une chaine de caract�res repr�sentant
	 * un message suivant :
	 * "OK" si l'enregistrement est r�ussi
	 * "ERREUR_100" si l'adresse mail est d�j� utilis�e
	 * "ERREUR_110" si l'adresse mail est invalide
	 * "ERREUR_200" si l'adresse postale n'est pas connue d'OSM
	 * 
	 * @param lastName le nom de famille
	 * @param firstName le pr�nom
	 * @param mail l'adresse mail
	 * @param mailingAddress l'adresse postale
	 * @return
	 */
	public Element postTeacher(LastName lastName, FirstName firstName,
			Mail mail, MailingAddress mailingAddress) throws Exception;
	
}
