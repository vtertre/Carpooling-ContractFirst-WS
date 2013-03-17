package iaws.covoiturage.dao;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;

import iaws.covoiturage.domain.Teacher;

public class TeacherCouchDB extends DAOCouchDB<Teacher> {

	public TeacherCouchDB(Database db) {
		super(db);
	}

	@Override
	public void insert(Teacher item) {
		
		Document doc = new Document();
		
		doc.put("_id", item.getLastName().getName() +
				item.getFirstName().getName() +
				item.getMailingAddress().getCode());
		doc.put("Nom", item.getLastName().getName());
	    doc.put("Prenom", item.getFirstName().getName());
	    doc.put("Mail", item.getMail().toString());
	    doc.put("AdressePostale", item.getMailingAddress().toString());
	    doc.put("Latitude", item.getCoordinates().getLatitude());
	    doc.put("Longitude", item.getCoordinates().getLongitude());
	    
	    db.saveDocument(doc);
	}

}
