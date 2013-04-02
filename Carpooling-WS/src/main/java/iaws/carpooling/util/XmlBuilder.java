package iaws.carpooling.util;

import iaws.carpooling.domain.Teacher;

import java.util.ArrayList;

import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Offre des operations de construction de fichiers
 * XML
 */
public class XmlBuilder {

	/**
	 * Namespace des fichiers XMl a generer
	 */
	private static Namespace ns = Namespace
			.getNamespace("http://iaws/ws/contractfirst/carpooling");

	/**
	 * Construit un Element XML a partir d'une liste
	 * d'enseignants.
	 * 
	 * @param list la liste d'enseignants
	 * @return un Element XML
	 */
	public static Element getElementFromNeighborsList(ArrayList<Teacher> list) {
		Element root = new Element("VicinityDetails", ns);

		for (Teacher t : list) {
			Element id = new Element("Id", ns);
			id.addContent(new Element("Lastname", ns).setText(t.getLastName()
					.getName()));
			id.addContent(new Element("Firstname", ns).setText(t.getFirstName()
					.getName()));

			Element mail = new Element("Mail", ns);
			mail.addContent(new Element("Perso", ns).setText(t.getMail()
					.getName()));
			mail.addContent(new Element("Domain", ns).setText(t.getMail()
					.getDomain()));

			Element teacher = new Element("Teacher", ns);
			teacher.addContent(id);
			teacher.addContent(mail);

			root.addContent(teacher);
		}

		return root;
	}

	/**
	 * Construit un Element XML a partir d'un code
	 * de retour d'une inscription
	 * 
	 * @param out le resultat de l'operation
	 * @return un Element XML
	 */
	public static Element getElementFromInscriptionCode(String[] out) {		
		Element root = new Element("InscriptionDetails", ns);
		
		root.addContent(new Element("Operation", ns).setText(out[0]));
		root.addContent(new Element("Code", ns).setText(out[1]));
		root.addContent(new Element("Message", ns).setText(out[2]));
		
		return root;
	}

}
