package iaws.carpooling.dao;

public abstract class DAOCouchDB<T> implements DAO<T> {
	
	/**
	 * URL de la base de type CouchDB
	 */
	protected String dbUrl;
	
	public DAOCouchDB(String dbUrl) {
		this.dbUrl = dbUrl;		
	}

}
