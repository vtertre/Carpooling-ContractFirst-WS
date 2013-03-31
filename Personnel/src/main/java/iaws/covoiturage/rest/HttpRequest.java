package iaws.covoiturage.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

public class HttpRequest {
	
	/**
	 * Envoie une requete HTTP GET et retourne
	 * la reponse du serveur.
	 * 
	 * @param query la requete a envoyer
	 * @return la reponse du serveur
	 */
	public static String httpGetRequest(String query) {		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(query);
		 
		HttpResponse response = null;
		
		try {
			response = httpClient.execute(get);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return parseServerResponse(response);
	}
	
	/**
	 * Envoie une requete HTTP POST et retourne
	 * la reponse du serveur.
	 * 
	 * @param url l'url
	 * @param object l'objet a attacher a la requete
	 * @return la reponse du serveur
	 */
	public static String httpPostRequest(String url, StringEntity object) {		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		request.setEntity(object);
		
		HttpResponse response = null;
		
		try {
			request.setHeader(new BasicHeader("Content-Type", "application/json"));
			response = httpClient.execute(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return parseServerResponse(response);
	}
	
	/**
	 * Envoie une requete HTTP DELETE et retourne
	 * la reponse du serveur
	 * 
	 * @param url l'utl
	 * @return la reponse du serveur
	 */
	public static String httpDeleteRequest(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete del = new HttpDelete(url);
		 
		HttpResponse response = null;
		
		try {
			response = httpClient.execute(del);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return parseServerResponse(response);
	}
	
	/**
	 * Parse la reponse d'un serveur a une requete http
	 * 
	 * @param response la reponse du serveur
	 * @return la reponse parsee
	 */
	private static String parseServerResponse(HttpResponse response) {
		
		String serverResponse = "";
		try {
			InputStream in = response.getEntity().getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String dataResponse = null;
			
			while ((dataResponse = reader.readLine()) != null)
				serverResponse += dataResponse;
			
			reader.close();			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return serverResponse;
	}

}
