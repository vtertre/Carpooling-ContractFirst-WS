package iaws.covoiturage.dao;

import java.util.ResourceBundle;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Session;

public final class DBConnection {
	
	private static Database db;
	
	private static String ip;
	private static int port;
	private static String db_name;
	
	static {
        ResourceBundle bundle = ResourceBundle.getBundle("db");

        ip = bundle.getString("db.ip");
        port = Integer.valueOf(bundle.getString("db.port"));
        db_name = bundle.getString("db.name");
    }
	
	private DBConnection() {
		Session dbSession = new Session(ip, port);
		db = dbSession.getDatabase(db_name);
    }
    
    public static Database getInstance() {
        if (db == null) {
            new DBConnection();
        }
        return db;
    }

}
