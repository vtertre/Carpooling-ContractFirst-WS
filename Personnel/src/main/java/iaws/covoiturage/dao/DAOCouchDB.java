package iaws.covoiturage.dao;

public abstract class DAOCouchDB<T> implements DAO<T> {
	
	protected String dbUrl;
	
	public DAOCouchDB(String dbUrl) {
		this.dbUrl = dbUrl;		
	}

}
