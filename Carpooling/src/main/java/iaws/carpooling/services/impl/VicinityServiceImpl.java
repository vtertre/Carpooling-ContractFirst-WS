package iaws.carpooling.services.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import iaws.carpooling.dao.DBUrl;
import iaws.carpooling.domain.Teacher;
import iaws.carpooling.domain.nomenclature.Coordinate;
import iaws.carpooling.domain.nomenclature.FirstName;
import iaws.carpooling.domain.nomenclature.LastName;
import iaws.carpooling.domain.nomenclature.Mail;
import iaws.carpooling.exception.UnregisteredIDException;
import iaws.carpooling.rest.HttpRequest;
import iaws.carpooling.services.VicinityService;

/**
 * Implementation du service de recherche de voisins
 */
public class VicinityServiceImpl implements VicinityService {

	/** 
	 * @see VicinityService#getNeighbors(String int)
	 */
	public ArrayList<Teacher> getNeighbors(String id, int radius) throws UnregisteredIDException {
		Coordinate userCoordinates;
		
		if ((userCoordinates = getUserCoordinates(id)) == null)
			throw new UnregisteredIDException("Identifiant non enregistre en base.");

		return getTeachersWithinRadius(userCoordinates, radius);
	}
	
	/**
	 * Recherche tous les utilisateurs enregistres
	 * et construit la liste de voisins a proximite
	 * 
	 * @param coord les coordonnees de l'utilisateur effectuant la demande
	 * @param radius la distance recherchee
	 * 
	 * @return la liste de voisins trouves
	 */
	private ArrayList<Teacher> getTeachersWithinRadius(Coordinate coord, int radius) {
		
		String url = DBUrl.getUrl() + "/_design"
				+ "/allview/_view/allDocs";
		String docs = "";
		try {
			docs = new String(HttpRequest.httpGetRequest(url).getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return searchTeachers(docs, coord, radius);
	}
	
	/**
	 * Recherche les voisins en fonction de la reponse renvoyee
	 * par le serveur.
	 * 
	 * @param jsonResponse la reponse du serveur
	 * @param coord les coordonnees de l'utilisateur effectuant la demande
	 * @param radius la distance recherchee
	 * 
	 * @return la liste de voisins trouves
	 */
	private ArrayList<Teacher> searchTeachers(String jsonResponse, Coordinate coord, int radius) {
		ArrayList<Teacher> list = new ArrayList<Teacher>();
		JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonResponse);
		JSONArray rows = json.getJSONArray("rows");

		// Pas de documents dans la base
		if (rows.size() <= 0)
			return list;
		
		// On regarde la distance pour chaque document
		for (int i = 0; i < rows.size(); ++i) {
			double lat = Double.valueOf(rows.getJSONObject(i).getJSONObject("value").
					getJSONObject("Coordonnees").getString("Latitude"));

			double lon = Double.valueOf(rows.getJSONObject(i).getJSONObject("value").
					getJSONObject("Coordonnees").getString("Longitude"));
			
			double dist = calculateDistance(coord.getLatitude(), coord.getLongitude(),
					lat, lon);
			
			// Si la distance correspond a la demande on ajoute le voisin a la liste
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
	
	/**
	 * Calcule et retourne la distance en km entre deux
	 * lieux geographiques localises par leur latitude et
	 * longitude.
	 * 
	 * @param lat1 latitude du premier lieu
	 * @param lon1 longitude du premier lieu
	 * @param lat2 latitude du deuxieme lieu
	 * @param lon2 longitude du deuxieme lieu
	 * 
	 * @return la distance calculee
	 */
	private double calculateDistance(double lat1, double lon1,
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

	/**
	 * Recherche les coordonnees d'un utilisateur
	 * enregistre en base de donnees.
	 * 
	 * @param id l'id a rechercher
	 * 
	 * @return les coordinnes trouvees
	 */
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
	
	/**
	 * Recherche les coordonnees d'un utilisateur
	 * dans le reponse du serveur.
	 * 
	 * @param jsonResponse la reponse du serveur
	 * 
	 * @return les coordonnes si l'utilisateur existe, null sinon
	 */
	private Coordinate getCoordinatesFromResponse(String jsonResponse) {
		JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonResponse);
		JSONArray rows = json.getJSONArray("rows");

		// Aucun resultat trouve
		if (rows.size() <= 0)
			return null;

		String lat = rows.getJSONObject(0).getJSONObject("value").
				getString("Latitude");

		String lon = rows.getJSONObject(0).getJSONObject("value").
				getString("Longitude");
		
		return new Coordinate(Double.valueOf(lat), Double.valueOf(lon));
	}

}
