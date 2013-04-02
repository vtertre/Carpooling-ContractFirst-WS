package iaws.carpooling.ws.contractfirst;

import iaws.carpooling.domain.nomenclature.FirstName;
import iaws.carpooling.domain.nomenclature.LastName;
import iaws.carpooling.domain.nomenclature.Mail;
import iaws.carpooling.domain.nomenclature.MailingAddress;
import iaws.carpooling.services.InscriptionService;
import iaws.carpooling.util.XmlBuilder;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Endpoint pour un demande d'inscription d'enseignant
 */
@Endpoint
public class InscriptionEndpoint {
	
	private InscriptionService inscriptionService;
	
	private static final String NAMESPACE_URI = "http://iaws/ws/contractfirst/carpooling";
	
	public InscriptionEndpoint(InscriptionService inscriptionService) {
		this.inscriptionService = inscriptionService;
	}
	
	@PayloadRoot(localPart = "InscriptionRequest", namespace = NAMESPACE_URI)
	@ResponsePayload
	public Element handleInscriptionRequest(@RequestPayload Element root) throws Exception {
		
		Namespace ns = Namespace.getNamespace(NAMESPACE_URI);
		
		LastName lastName = new LastName(root.getChild("Identite", ns).getChildText("Nom", ns));
		
		FirstName firstName = new FirstName(root.getChild("Identite", ns).getChildText("Prenom", ns));
		
		Mail mail = new Mail(root.getChild("Mail", ns).getChildText("Perso", ns),
				root.getChild("Mail", ns).getChildText("Domaine", ns));
		
		MailingAddress mailingAddress = new MailingAddress(
				root.getChild("AdressePostale", ns).getChildText("Rue", ns),
				root.getChild("AdressePostale", ns).getChildText("Code", ns),
				root.getChild("AdressePostale", ns).getChildText("Ville", ns));
		
		String codeOp = inscriptionService.postTeacher(lastName, firstName, mail, mailingAddress);
		
		return XmlBuilder.getElementFromInscriptionCode(codeOp);
	}

}
