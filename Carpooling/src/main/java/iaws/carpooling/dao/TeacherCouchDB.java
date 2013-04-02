package iaws.carpooling.dao;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import org.apache.http.entity.StringEntity;

import iaws.carpooling.domain.Teacher;
import iaws.carpooling.rest.HttpRequest;

/**
 * Gestion des acces en base de donnees
 * pour un enseignant
 */
public class TeacherCouchDB extends DAOCouchDB<Teacher> {

	public TeacherCouchDB(String dbUrl) {
		super(dbUrl);
	}

	/**
	 * @see DAO#insert(Object)
	 */
	public void insert(Teacher item) {
		
		// On cree l'objet JSON a ajouter
		JSONObject doc = new JSONObject();
		StringEntity entity = null;

		try {
			doc.put("_id", item.getMail().getName());
			doc.put("Lastname", item.getLastName().getName());
			doc.put("Firstname", item.getFirstName().getName());
			doc.put("Mail", item.getMail().toString());
			doc.put("MailingAddress", item.getMailingAddress().toString());
			JSONObject coord = new JSONObject();
			coord.put("Latitude", item.getCoordinates().getLatitude());
			coord.put("Longitude", item.getCoordinates().getLongitude());
			doc.put("Coordinates", coord);

			entity = new StringEntity(doc.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// Requete HTTP POST
		HttpRequest.httpPostRequest(dbUrl, entity);
	}

}
