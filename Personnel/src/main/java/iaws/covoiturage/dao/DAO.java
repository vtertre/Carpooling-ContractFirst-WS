package iaws.covoiturage.dao;

/**
 * Interface d'accès aux objets en base de type CouchDB
 * 
 * @param <T>
 */
public interface DAO<T> {
	
	/**
	 * Enregistre un objet en base de donnees
	 * 
	 * @param item l'objet a enregistrer
	 */
	void insert(T item);

}
