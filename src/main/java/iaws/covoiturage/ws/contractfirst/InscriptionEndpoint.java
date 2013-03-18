package iaws.covoiturage.ws.contractfirst;

import iaws.covoiturage.domain.nomenclature.FirstName;
import iaws.covoiturage.domain.nomenclature.LastName;
import iaws.covoiturage.domain.nomenclature.Mail;
import iaws.covoiturage.domain.nomenclature.MailingAddress;
import iaws.covoiturage.services.InscriptionService;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;
import org.w3c.dom.Element;

@Endpoint
public class InscriptionEndpoint {
	
	private InscriptionService inscriptionService;
	
	private static final String NAMESPACE_URI = "http://iaws/ws/contractfirst/covoiturage";
	
	public InscriptionEndpoint(InscriptionService inscriptionService) {
		this.inscriptionService = inscriptionService;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "InscriptionRequest")
	@ResponsePayload
	public Element handleInscriptionRequest(@XPathParam("/InscriptionRequest/Identite/Nom") String lName,
			@XPathParam("/InscriptionRequest/Identite/Prenom") String fName,
			@XPathParam("/InscriptionRequest/Mail/Perso") String perso,
			@XPathParam("/InscriptionRequest/Mail/Domaine") String domain,
			@XPathParam("/InscriptionRequest/AdressePostale/Rue") String street,
			@XPathParam("/InscriptionRequest/AdressePostale/Code") String code,
			@XPathParam("/InscriptionRequest/AdressePostale/Ville") String city) throws Exception {
		
		System.out.println("lName: " + lName + "||");
		System.out.println("fName: " + fName + "||");
		System.out.println("Perso: " + perso + "||");
		System.out.println("domain: " + domain + "||");
		System.out.println("street: " + street + "||");
		System.out.println("code: " + code + "||");
		System.out.println("city: " + city + "||");
		
		LastName lastName = new LastName(lName);
		FirstName firstName = new FirstName(fName);
		Mail mail = new Mail(perso, domain);
		MailingAddress mailingAddress = new MailingAddress(street, code, city);
		
		Element resp = inscriptionService.postTeacher(lastName, firstName, mail, mailingAddress);
		
		return resp;
	}

}
