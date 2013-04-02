package iaws.carpooling.util;

import java.util.ArrayList;

import iaws.carpooling.domain.Teacher;
import iaws.carpooling.domain.nomenclature.FirstName;
import iaws.carpooling.domain.nomenclature.LastName;
import iaws.carpooling.domain.nomenclature.Mail;

import org.jdom2.Element;

import junit.framework.TestCase;

public class TestXmlBuilder extends TestCase {

	public void testGetElementFromNeighborsList() {
		ArrayList<Teacher> al = new ArrayList<Teacher>();
		LastName lastName = new LastName("Test1Nom");
		FirstName firstName = new FirstName("Test1Prenom");
		Mail mail = new Mail("test1", "univ-tlse3.fr");
		Teacher teacher = new Teacher(lastName, firstName, mail);
		al.add(teacher);

		Element elem = XmlBuilder.getElementFromNeighborsList(al);
		
		assert elem != null;
	}
	
	public void testGetElementFromInscriptionCode() {
		String code = "OK";
		Element elem = XmlBuilder.getElementFromInscriptionCode(code);
		
		assert elem != null;
	}

}
