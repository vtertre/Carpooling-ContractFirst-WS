package iaws.covoiturage.services.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jdom2.Element;
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
import iaws.covoiturage.services.VicinityService;
import iaws.covoiturage.ws.contractfirst.XmlHelper;

public class VicinityServiceImpl implements VicinityService {

	/**
	 * @see VicinityService#getNeighbors(String int)
	 */
	@Override
	public Element getNeighbors(String id, int radius) throws Exception {
		LastName lastName;
		FirstName firstName;
		int code = 0;

		// Un ID est de la forme LastnameFirstnameCodepostal
		if (Character.isUpperCase(id.charAt(0))) {
			for (int i = 1; i < id.length(); i++) {
				if(Character.isUpperCase(id.charAt(i))) {
					lastName = new LastName(id.substring(0, i-1));
					code = i;
				} else if (Character.isDigit(id.charAt(i))) {
					firstName = new FirstName(id.substring(code, i-1));
					code = Integer.parseInt(id.substring(i));
				}
			}
		}
		// TODO ajouter la requête
		String response = HttpRequest.httpGetRequest(DBUrl.getUrl());
		
			return null;
	}



	/**
	 * Recherche les coordonnees d'une adresse postale avec OSM
	 * 
	 * @param mailingAddress
	 *            l'adresse postale a verifier
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

		safeUrl = "http://nominatim.openstreetmap.org/search?q=" + safeUrl
				+ "&format=xml";

		// Requete de type GET
		String response = HttpRequest.httpGetRequest(safeUrl);

		return getCoordinatesFromXML(response);
	}

	/**
	 * Recherche la Latitude & la Longitude dans une reponse XML d'OSM.
	 * 
	 * @param xmlString
	 *            la reponse a la requete GET
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

			coord = new Coordinate(Double.valueOf(xmlDoc.getRootElement()
					.getChild("place").getAttribute("lat").getValue()),
					Double.valueOf(xmlDoc.getRootElement().getChild("place")
							.getAttribute("lon").getValue()));

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return coord;
	}

}
