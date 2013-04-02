package iaws.carpooling.services.impl;

import iaws.carpooling.domain.nomenclature.FirstName;
import iaws.carpooling.domain.nomenclature.LastName;
import iaws.carpooling.domain.nomenclature.Mail;
import iaws.carpooling.domain.nomenclature.MailingAddress;
import iaws.carpooling.services.InscriptionService;
import junit.framework.TestCase;

public class TestInscriptionServiceImpl extends TestCase {
	
	public void testPostTeacher() throws Exception {
		LastName ln = new LastName("TestNom");
		
		FirstName fn = new FirstName("TestPrenom");
		
		Mail mail = new Mail("test", "univ-tlse3.fr");
		
		MailingAddress ma = new MailingAddress(
				"Universite Paul Sabatier",	"31000", "Toulouse");
		
		InscriptionService sv = new InscriptionServiceImpl();
		String ret = sv.postTeacher(ln, fn, mail, ma);
		
		assert ret.equals("OK") || ret.equals("ERREUR_100");
	}

}
