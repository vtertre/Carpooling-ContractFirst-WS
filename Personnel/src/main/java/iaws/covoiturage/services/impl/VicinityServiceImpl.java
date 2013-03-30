package iaws.covoiturage.services.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import iaws.covoiturage.dao.DBUrl;
import iaws.covoiturage.domain.Teacher;
import iaws.covoiturage.domain.nomenclature.Coordinate;
import iaws.covoiturage.domain.nomenclature.FirstName;
import iaws.covoiturage.domain.nomenclature.LastName;
import iaws.covoiturage.domain.nomenclature.Mail;
import iaws.covoiturage.exception.UnregisteredIDException;
import iaws.covoiturage.rest.HttpRequest;
import iaws.covoiturage.services.VicinityService;

public class VicinityServiceImpl implements VicinityService {

	/**
	 * @throws UnregisteredIDException 
	 * @see VicinityService#getNeighbors(String int)
	 */
	public ArrayList<Teacher> getNeighbors(String id, int radius) throws UnregisteredIDException {
		Coordinate userCoordinates;
		
		if ((userCoordinates = getUserCoordinates(id)) == null)
			throw new UnregisteredIDException("Identifiant non enregistré en base.");

		return getTeachersWithinRadius(userCoordinates, radius);
	}
	
	private ArrayList<Teacher> getTeachersWithinRadius(Coordinate coord, int radius) {
		
		String url = DBUrl.getUrl() + "/_design"
				+ "/allview/_view/allDocs";
		String docs = "";
		try {
			docs = new String(HttpRequest.httpGetRequest(url).getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//System.out.println(docs);
		
		return searchTeachers(docs, coord, radius);
	}
	
	private ArrayList<Teacher> searchTeachers(String jsonResponse, Coordinate coord, int radius) {
		ArrayList<Teacher> list = new ArrayList<>();
		JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonResponse);
		JSONArray rows = json.getJSONArray("rows");
		
		//System.out.println(jsonResponse);

		if (rows.size() <= 0) {
			System.out.println("Aucun documents dans la base.");
			return list;
		}
		
		for (int i = 0; i < rows.size(); ++i) {
			double lat = Double.valueOf(rows.getJSONObject(i).getJSONObject("value").
					getJSONObject("Coordonnees").getString("Latitude"));

			double lon = Double.valueOf(rows.getJSONObject(i).getJSONObject("value").
					getJSONObject("Coordonnees").getString("Longitude"));
			
			double dist = calculateDistance(coord.getLatitude(), coord.getLongitude(),
					lat, lon);
			
			if (dist <= radius && dist != 0D) {
				
				LastName ln = new LastName(rows.getJSONObject(i).getJSONObject("value").getString("Nom"));
				FirstName fn = new FirstName(rows.getJSONObject(i).getJSONObject("value").getString("Prenom"));
				
				String perso = rows.getJSONObject(i).getJSONObject("value").getString("Mail");				
				int ind = perso.indexOf("@");
				Mail mail = new Mail(perso.substring(0, ind),
						perso.substring(ind + 1, perso.length()));
				
				list.add(new Teacher(ln, fn, mail));
			}
		}		
		
		return list;
	}
	
	private static double calculateDistance(double lat1, double lon1,
			double lat2, double lon2) {
		
		double earthRadius = 6371D;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.sin(dLon / 2) * Math.sin(dLon / 2) *
				Math.cos(lat1) * Math.cos(lat2);
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		
		return earthRadius * c;
	}

	private Coordinate getUserCoordinates(String id) {

		// Encodage de l'adresse mail
		String safeUrl = "\"" + id + "\"";
		try {
			safeUrl = URLEncoder.encode(safeUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		safeUrl = DBUrl.getUrl() + "/_design"
				+ "/coordview/_view/coordinates?key=" + safeUrl;

		// Requete de type GET
		String response = HttpRequest.httpGetRequest(safeUrl);

		return getCoordinatesFromResponse(response);
	}
	
	private Coordinate getCoordinatesFromResponse(String jsonResponse) {
		JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonResponse);
		JSONArray rows = json.getJSONArray("rows");
		
		//System.out.println(jsonResponse);

		if (rows.size() <= 0)
			return null;

		String lat = rows.getJSONObject(0).getJSONObject("value").
				getString("Latitude");

		String lon = rows.getJSONObject(0).getJSONObject("value").
				getString("Longitude");
		
		return new Coordinate(Double.valueOf(lat), Double.valueOf(lon));
	}
	
	
	//________________________________________________________________________

	/*public static void main(String args[]) {
		Session s = new Session("localhost", 5984);
		Database db = s.getDatabase("iaws_ws_covoiturage");
		Document doc = new Document();
		doc.setId("_design/coordview");
				                 
		String str = "{\"coordinates\": {\"map\": \"function(doc) { if (doc._id)  emit(doc.Mail, doc.Coordonnees) } \"}}";
				         
		doc.put("views", str); 
		db.saveDocument(doc);
	}*/

}
