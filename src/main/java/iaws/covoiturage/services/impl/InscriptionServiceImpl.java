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
		
		// Verification du domaine
		if (!mail.getDomain().equals("univ-tlse3.fr"))
			return XmlHelper.getRootElementFromFileInClasspath("InscriptionDetails110.xml");
		
		// Verification de l'unicite
		if (checkMailUsed(mail.toString()))
			return XmlHelper.getRootElementFromFileInClasspath("InscriptionDetails100.xml");
		
		// Compatibilite OSM
		if ((addressCoord = getAddressCoordinates(mailingAddress.toString())) == null)
			return XmlHelper.getRootElementFromFileInClasspath("InscriptionDetails200.xml");
		
		Teacher teacher = new Teacher(lastName, firstName, mail, mailingAddress, addressCoord);
		
		// TODO A voir requete PUT au lieu d'utilisation lib
		// Enregistrement DB
		DAOCouchDB<Teacher> teacherDAO = new TeacherCouchDB(DBConnection.getInstance());		
		teacherDAO.insert(teacher);
		
		return XmlHelper.getRootElementFromFileInClasspath("InscriptionDetailsOK.xml");
	}
	
	/**
	 * Verifie l'unicite d'une adresse mail dans la base
	 * 
	 * @param email l'adresse a verifier
	 * @return true si l'adresse est deja utilisee, false sinon
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
		
		// Requ�te de type GET
		String response = QueryBuilder.httpGetQuery(safeUrl);
		
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
		
		// Requete de type GET
		String response = QueryBuilder.httpGetQuery(safeUrl);
		
		return getCoordinatesFromXML(response);
	}
	
	/**
	 * Recherche la Latitude & la Longitude dans une reponse
	 * XML d'OSM.
	 * 
	 * @param xmlString la reponse a la requete GET
	 * @return null si le lieu n'est pas repertorie, les coordonnees sinon
	 * 
	 * @see QueryBuilder#httpGetQuery(String)
	 */
	private Coordinate getCoordinatesFromXML(String xmlString) {
		
		Coordinate coord = null;
		
		try {
			SAXBuilder xmlBuilder = new SAXBuilder();
			Reader in = new StringReader(xmlString);
			org.jdom2.Document xmlDoc = xmlBuilder.build(in);
			
			// TODO verifier qu'on a bien une reponse pour lieu recherche
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
	
	/*public static void main(String args[]) {
		LastName ln = new LastName("Boul");
		FirstName fn = new FirstName("Rash");
		Mail mail = new Mail("rash.boul", "univ-tlse3.fr");
		MailingAddress ma = new MailingAddress("Universite Paul Sabatier", "31000", "Toulouse");
		
		try {
			System.out.println(new InscriptionServiceImpl().postTeacher(ln, fn, mail, ma));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}
