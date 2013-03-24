package iaws.covoiturage.dao;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import iaws.covoiturage.domain.Teacher;
import iaws.covoiturage.rest.HttpRequest;

public class TeacherCouchDB extends DAOCouchDB<Teacher> {

	public TeacherCouchDB(String dbUrl) {
		super(dbUrl);
	}

	@Override
	public void insert(Teacher item) {

		JSONObject doc = new JSONObject();
		StringEntity entity = null;

		try {
			doc.put("_id", item.getLastName().getName()
					+ item.getFirstName().getName()
					+ item.getMailingAddress().getCode());
			doc.put("Nom", item.getLastName().getName());
			doc.put("Prenom", item.getFirstName().getName());
			doc.put("Mail", item.getMail().toString());
			doc.put("AdressePostale", item.getMailingAddress().toString());
			doc.put("Latitude", item.getCoordinates().getLatitude());
			doc.put("Longitude", item.getCoordinates().getLongitude());
			
			entity = new StringEntity(doc.toString(), "UTF-8");			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String response = HttpRequest.httpPostRequest(dbUrl, entity);
		System.out.println(response);
	}
}
