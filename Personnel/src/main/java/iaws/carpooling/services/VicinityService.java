package iaws.carpooling.services;

import iaws.carpooling.domain.Teacher;
import iaws.carpooling.exception.UnregisteredIDException;

import java.util.ArrayList;


public interface VicinityService {

	/**
	 * Recupere la liste des voisins de l'utilisateur passe en parametre
	 * 
	 * @param id
	 *            l'identifiant de l'utilisateur
	 * @param radius
	 *            le rayon de voisinnage en kilometres
	 *            
	 * @return la liste des professeurs dans le rayon demane
	 * 
	 * @throws UnregisteredIDException 
	 */
	public ArrayList<Teacher> getNeighbors(String id, int radius)
			throws UnregisteredIDException;

}
