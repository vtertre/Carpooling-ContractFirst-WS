package iaws.covoiturage.services.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import iaws.covoiturage.dao.DAOCouchDB;
import iaws.covoiturage.dao.DBConnection;
import iaws.covoiturage.dao.TeacherCouchDB;
import iaws.covoiturage.domain.Teacher;
import iaws.covoiturage.domain.nomenclature.Coordinate;
import iaws.covoiturage.domain.nomenclature.FirstName;
import iaws.covoiturage.domain.nomenclature.LastName;
import iaws.covoiturage.domain.nomenclature.Mail;
import iaws.covoiturage.domain.nomenclature.MailingAddress;
import iaws.covoiturage.http.QueryBuilder;
import iaws.covoiturage.services.InscriptionService;
import iaws.covoiturage.ws.contractfirst.XmlHelper;


public class InscriptionServiceImpl implements InscriptionService {

	/**
	 * @throws SAXException 
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws Exception 
	 * @see InscriptionService#postTeacher(LastName, FirstName, Mail, MailingAddress)
	 */
	public Element postTeacher(LastName lastName, FirstName firstName,
			Mail mail, MailingAddress mailingAddress) throws Exception {
		
		Coordinate addressCoord;
		
		// Vérification du domaine
		if (!mail.getDomain().equals("univ-tlse3.fr"))
			return XmlHelper.getRootElementFromFileInClasspath("InscriptionDetails110.xml");
		
		// Vérification de l'unicité
		if (checkMailUsed(mail.toString()))
			return XmlHelper.getRootElementFromFileInClasspath("InscriptionDetails100.xml");
		
		// Compatibilité OSM
		if ((addressCoord = getAddressCoordinates(mailingAddress.toString())) == null)
			return XmlHelper.getRootElementFromFileInClasspath("InscriptionDetails200.xml");
		
		Teacher teacher = new Teacher(lastName, firstName, mail, mailingAddress, addressCoord);
		
		// TODO A voir requête PUT au lieu d'utilisation lib
		// Enregistrement DB
		DAOCouchDB<Teacher> teacherDAO = new TeacherCouchDB(DBConnection.getInstance());		
		teacherDAO.insert(teacher);
		
		return XmlHelper.getRootElementFromFileInClasspath("InscriptionDetailsOK.xml");
	}
	
	/**
	 * Vérifie l'unicité d'une adresse mail dans la base
	 * 
	 * @param email l'adresse à vérifier
	 * @return true si l'adresse est déjà utilisée, false sinon
	 */
	private boolean checkMailUsed(String email) {
		
		ResourceBundle res = ResourceBundle.getBundle("db");
		
		String ip = res.getString("db.ip");
		int port = Integer.valueOf(res.getString("db.port"));
		String db_name = res.getString("db.name");
		 
		// Encodage de l'adresse mail
		String safeUrl = "\"" + email + "\"";
		try {
			safeUrl = URLEncoder.encode(safeUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		safeUrl = "http://" + ip + ":" + port + "/" + db_name + "/_design" +
				"/mailview/_view/emailAddress?key=" + safeUrl;
		
		// Requête de type GET
		String response = QueryBuilder.httpGetQuery(safeUrl);
		
		// TODO Paser le JSon ... ?
		if (response.contains(email))
			return true;
		
		return false;
	}
	
	/**
	 * Recherche les coordonnées d'une adresse postale
	 * avec OSM
	 * 
	 * @param mailingAddress l'adresse postale à vérifier
	 * @return les coordonnées (latitude, longitude)
	 * 
	 * @see QueryBuilder#httpGetQuery(String)
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
		
		// Requête de type GET
		String response = QueryBuilder.httpGetQuery(safeUrl);
		
		return getCoordinatesFromXML(response);
	}
	
	/**
	 * Recherche la Latitude & la Longitude dans une réponse
	 * XML d'OSM.
	 * 
	 * @param xmlString la réponse à la requête GET
	 * @return null si le lieu n'est pas répertorié, les coordonnées sinon
	 * 
	 * @see QueryBuilder#httpGetQuery(String)
	 */
	private Coordinate getCoordinatesFromXML(String xmlString) {
		
		Coordinate coord = null;
		
		try {
			SAXBuilder xmlBuilder = new SAXBuilder();
			Reader in = new StringReader(xmlString);
			org.jdom2.Document xmlDoc = xmlBuilder.build(in);
			
			// TODO vérifier qu'on a bien une réponse pour lieu recherché
			// PAS SECURE
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
	
	public static void main(String args[]) {
		LastName ln = new LastName("Boul");
		FirstName fn = new FirstName("Rash");
		Mail mail = new Mail("rash.boul", "univ-tlse3.fr");
		MailingAddress ma = new MailingAddress("Université Paul Sabatier", "31000", "Toulouse");
		
		try {
			System.out.println(new InscriptionServiceImpl().postTeacher(ln, fn, mail, ma));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
