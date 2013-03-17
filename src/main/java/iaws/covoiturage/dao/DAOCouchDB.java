package iaws.covoiturage.dao;

import com.fourspaces.couchdb.Database;

public abstract class DAOCouchDB<T> implements DAO<T> {
	
	protected Database db;
	
	public DAOCouchDB(Database db) {
		this.db = db;		
	}

}
