package iaws.covoiturage.ws.contractfirst;

import java.util.ArrayList;

import iaws.covoiturage.domain.Teacher;
import iaws.covoiturage.services.VicinityService;
import iaws.covoiturage.util.XmlBuilder;

import org.jdom2.Element;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;

/**
 * Endpoint pour une demande de liste de voisins
 */
@Endpoint
public class VicinityEndpoint {
	
	private VicinityService vicinityService;
	
	private static final String NAMESPACE_URI = "http://iaws/ws/contractfirst/covoiturage";
	
	public VicinityEndpoint(VicinityService vicinityService) {
		this.vicinityService = vicinityService;
	}
	
	@PayloadRoot(localPart = "VicinityRequest", namespace = NAMESPACE_URI)
	@Namespace(prefix = "cov", uri = NAMESPACE_URI)
	@ResponsePayload
	public Element handleInscriptionRequest(@XPathParam("/cov:VicinityRequest/cov:Identifiant") String id,
			@XPathParam("/cov:VicinityRequest/cov:Rayon") int radius) throws Exception {
		
		ArrayList<Teacher> neighbors = vicinityService.getNeighbors(id, radius);
		
		return XmlBuilder.getElementFromNeighborsList(neighbors);		
	}

}
