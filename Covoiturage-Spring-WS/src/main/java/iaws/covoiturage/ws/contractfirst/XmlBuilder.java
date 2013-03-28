package iaws.covoiturage.ws.contractfirst;

import iaws.covoiturage.domain.Teacher;

import java.util.ArrayList;

import org.jdom2.Element;
import org.jdom2.Namespace;

public class XmlBuilder {
	
	public static Element getElementFromNeighborsList(ArrayList<Teacher> list) {
		Namespace ns = Namespace.getNamespace(
				"http://iaws/ws/contractfirst/covoiturage");
		Element root = new Element("VicinityDetails", ns);
		
		for (Teacher t : list) {			
			Element lastName = new Element("Nom", ns);
			lastName.setText(t.getLastName().getName());
			
			Element firstName = new Element("Prenom", ns);
			firstName.setText(t.getFirstName().getName());
			
			Element perso = new Element("Perso", ns);
			perso.setText(t.getMail().getName());
			
			Element domain = new Element("Domaine", ns);
			domain.setText(t.getMail().getDomain());
			
			Element id = new Element("Identite", ns);
			id.addContent(lastName);
			id.addContent(firstName);
			
			Element mail = new Element("Mail", ns);
			mail.addContent(perso);
			mail.addContent(domain);
			
			Element teacher = new Element("Personnel", ns);
			teacher.addContent(id);
			teacher.addContent(mail);
			
			root.addContent(teacher);
		}
		
		return root;
	}

}
