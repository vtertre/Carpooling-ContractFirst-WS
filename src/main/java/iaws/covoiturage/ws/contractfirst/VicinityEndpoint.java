package iaws.covoiturage.ws.contractfirst;

import iaws.covoiturage.services.VicinityService;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class VicinityEndpoint {
	
	private VicinityService vicinityService;
	
	private static final String NAMESPACE_URI = "http://iaws/ws/contractfirst/covoiturage";
	
	public VicinityEndpoint(VicinityService vicinityService) {
		this.vicinityService = vicinityService;
	}
	
	@PayloadRoot(localPart = "VicinityRequest", namespace = NAMESPACE_URI)
	@ResponsePayload
	public Element handleInscriptionRequest(@RequestPayload Element root) throws Exception {
		
		Namespace ns = Namespace.getNamespace(NAMESPACE_URI);
		String id = root.getChild("Identifiant", ns).getText();
		int radius = Integer.parseInt(root.getChild("Rayon", ns).getText());
		
		return vicinityService.getNeighbors(id, radius);
		
	}

}
