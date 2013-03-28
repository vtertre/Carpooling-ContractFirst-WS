package iaws.covoiturage.ws.contractfirst;

import iaws.covoiturage.domain.nomenclature.FirstName;
import iaws.covoiturage.domain.nomenclature.LastName;
import iaws.covoiturage.domain.nomenclature.Mail;
import iaws.covoiturage.domain.nomenclature.MailingAddress;
import iaws.covoiturage.services.InscriptionService;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class InscriptionEndpoint {
	
	private InscriptionService inscriptionService;
	
	private static final String NAMESPACE_URI = "http://iaws/ws/contractfirst/covoiturage";
	
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
		
		return inscriptionService.postTeacher(lastName, firstName, mail, mailingAddress);
	}

}
