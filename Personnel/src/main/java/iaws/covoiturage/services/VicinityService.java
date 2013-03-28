package iaws.covoiturage.services;

import iaws.covoiturage.domain.Teacher;

import java.util.ArrayList;


public interface VicinityService {

	/**
	 * R�cup�re la liste des voisins de l'utilisateur pass� en param�tre
	 * 
	 * @param id
	 *            l'identifiant de l'utilisateur
	 * @param radius
	 *            le rayon de voisinnage en kilom�tres
	 * @return
	 */
	public ArrayList<Teacher> getNeighbors(String id, int radius) throws Exception;

}
