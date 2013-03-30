package iaws.covoiturage.services.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import iaws.covoiturage.dao.DAOCouchDB;
import iaws.covoiturage.dao.DBUrl;
import iaws.covoiturage.dao.TeacherCouchDB;
import iaws.covoiturage.domain.Teacher;
import iaws.covoiturage.domain.nomenclature.Coordinate;
import iaws.covoiturage.domain.nomenclature.FirstName;
import iaws.covoiturage.domain.nomenclature.LastName;
import iaws.covoiturage.domain.nomenclature.Mail;
import iaws.covoiturage.domain.nomenclature.MailingAddress;
import iaws.covoiturage.rest.HttpRequest;
import iaws.covoiturage.services.InscriptionService;


public class InscriptionServiceImpl implements InscriptionService {

	/**
	 * @see InscriptionService#postTeacher(LastName, FirstName, Mail, MailingAddress)
	 */
	public String postTeacher(LastName lastName, FirstName firstName,
			Mail mail, MailingAddress mailingAddress) throws Exception {
		
		Coordinate addressCoord;
		
		// Verification du domaine
		if (!mail.getDomain().equals("univ-tlse3.fr"))
			return "ERREUR_110";
		
		// Verification de l'unicite
		if (checkMailUsed(mail.toString()))
			return "ERREUR_100";
		
		// Compatibilite OSM
		if ((addressCoord = getAddressCoordinates(mailingAddress.toString())) == null)
			return "ERREUR_200";
		
		Teacher teacher = new Teacher(lastName, firstName, mail, mailingAddress, addressCoord);
		
		// Enregistrement DB
		DAOCouchDB<Teacher> teacherDAO = new TeacherCouchDB(DBUrl.getUrl());		
		teacherDAO.insert(teacher);
		
		return "OK";
	}
	
	/**
	 * Verifie l'unicite d'une adresse mail dans la base
	 * 
	 * @param email l'adresse a verifier
	 * @return true si l'adresse est deja utilisee, false sinon
	 */
	private boolean checkMailUsed(String email) {
		 
		// Encodage de l'adresse mail
		String safeUrl = "\"" + email + "\"";
		try {
			safeUrl = URLEncoder.encode(safeUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		safeUrl = DBUrl.getUrl() + "/_design" +
				"/mailview/_view/emailAddress?key=" + safeUrl;
		
		// Requete de type GET
		String response = HttpRequest.httpGetRequest(safeUrl);
		
		// TODO Paser le JSon ... ?
		if (response.contains(email))
			return true;
		
		return false;
	}
	
	/**
	 * Recherche les coordonnees d'une adresse postale
	 * avec OSM
	 * 
	 * @param mailingAddress l'adresse postale a verifier
	 * @return les coordonnees (latitude, longitude)
	 * 
	 * @see HttpRequest#httpGetQuery(String)
	 */
	private Coordinate getAddressCoordinates(String mailingAddress) {
		 
		// Encodage de l'adresse postale
		String safeUrl = mailingAddress;
		try {
			safeUrl = URLEncoder.encode(safeUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		safeUrl = "http://nominatim.openstreetmap.org/search?q=" +
				safeUrl + "&format=xml";
		
		// Requete de type GET
		String response = HttpRequest.httpGetRequest(safeUrl);
		
		return getCoordinatesFromXML(response);
	}
	
	/**
	 * Recherche la Latitude & la Longitude dans une reponse
	 * XML d'OSM.
	 * 
	 * @param xmlString la reponse a la requete GET
	 * @return null si le lieu n'est pas repertorie, les coordonnees sinon
	 * 
	 * @see HttpRequest#httpGetQuery(String)
	 */
	private Coordinate getCoordinatesFromXML(String xmlString) {
		
		Coordinate coord = null;
		
		try {
			SAXBuilder xmlBuilder = new SAXBuilder();
			Reader in = new StringReader(xmlString);
			org.jdom2.Document xmlDoc = xmlBuilder.build(in);
			
			// TODO verifier qu'on a bien une reponse pour lieu recherche
			if (xmlDoc.getRootElement().getChildren().isEmpty())
				return null;
			
			coord = new Coordinate(
					Double.valueOf(xmlDoc.getRootElement().getChild("place").getAttribute("lat").getValue()),
					Double.valueOf(xmlDoc.getRootElement().getChild("place").getAttribute("lon").getValue()));
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return coord;
	}
	
}
