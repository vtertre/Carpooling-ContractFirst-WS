package iaws.covoiturage.services;

import iaws.covoiturage.domain.Teacher;

import java.util.ArrayList;


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
	public ArrayList<Teacher> getNeighbors(String id, int radius) throws Exception;

}
