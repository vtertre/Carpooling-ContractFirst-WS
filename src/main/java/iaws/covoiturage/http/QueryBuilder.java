package iaws.covoiturage.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class QueryBuilder {
	
	/**
	 * Envoie une requête HTTP GET et retourne
	 * la réponse du serveur.
	 * 
	 * @param query la requête à envoyer
	 * @return la réponse du serveur
	 */
	public static String httpGetQuery(String query) {
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(query);
		 
		HttpResponse response;
		String queryResponse = "";
		try {
			response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			InputStream instream = entity.getContent();
			 
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			String dataResponse = null;
			 
			// Réponse à la requête
			while ((dataResponse = reader.readLine()) != null)
				queryResponse += dataResponse;
			
			reader.close();		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return queryResponse;
	}

}
