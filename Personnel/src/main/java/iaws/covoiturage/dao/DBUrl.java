package iaws.covoiturage.dao;

import java.util.ResourceBundle;

public final class DBUrl {
	
	private static String url = null;
	
	private static String ip;
	private static int port;
	private static String db_name;
	
	static {
        ResourceBundle bundle = ResourceBundle.getBundle("db");

        ip = bundle.getString("db.ip");
        port = Integer.valueOf(bundle.getString("db.port"));
        db_name = bundle.getString("db.name");
        
        url = "http://" + ip + ":" + port + "/" + db_name;
    }
    
    public static String getUrl() {
        return url;
    }

}
