package iaws.covoiturage.util;

import iaws.covoiturage.domain.Teacher;

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
			.getNamespace("http://iaws/ws/contractfirst/covoiturage");

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
			Element id = new Element("Identite", ns);
			id.addContent(new Element("Nom", ns).setText(t.getLastName()
					.getName()));
			id.addContent(new Element("Prenom", ns).setText(t.getFirstName()
					.getName()));

			Element mail = new Element("Mail", ns);
			mail.addContent(new Element("Perso", ns).setText(t.getMail()
					.getName()));
			mail.addContent(new Element("Domaine", ns).setText(t.getMail()
					.getDomain()));

			Element teacher = new Element("Personnel", ns);
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
	 * @param code le code de l'operation
	 * @return un Element XML
	 */
	public static Element getElementFromInscriptionCode(String code) {		
		return new Element("InscriptionDetails", ns).
				addContent(new Element("Code", ns).setText(code));
	}

}
