package iaws.covoiturage.dao;

import java.util.ResourceBundle;

public final class DBUrl {

	private static String url = null;

	static {
		ResourceBundle bundle = ResourceBundle.getBundle("db");

		String ip = bundle.getString("db.ip");
		int port = Integer.valueOf(bundle.getString("db.port"));
		String db_name = bundle.getString("db.name");

		url = "http://" + ip + ":" + port + "/" + db_name;
	}

	public static String getUrl() {
		return url;
	}

}
