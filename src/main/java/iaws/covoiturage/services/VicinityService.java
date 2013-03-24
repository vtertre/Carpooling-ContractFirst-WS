package iaws.covoiturage.services;

import org.jdom2.Element;


public interface VicinityService {

	/**
	 * Récupère la liste des voisins de l'utilisateur passé en paramètre
	 * 
	 * @param id
	 *            l'identifiant de l'utilisateur
	 * @param radius
	 *            le rayon de voisinnage en kilomètres
	 * @return
	 */
	public Element getNeighbors(String id, int radius) throws Exception;

}
