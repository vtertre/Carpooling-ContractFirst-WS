package iaws.covoiturage.dao;

public interface DAO<T> {
	
	/**
	 * Enregistre un objet en base de donnees
	 * 
	 * @param item l'objet a enregistrer
	 */
	void insert(T item);

}
