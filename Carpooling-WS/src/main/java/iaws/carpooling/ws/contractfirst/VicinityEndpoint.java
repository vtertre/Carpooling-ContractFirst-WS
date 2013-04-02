package iaws.carpooling.ws.contractfirst;

import java.util.ArrayList;

import iaws.carpooling.domain.Teacher;
import iaws.carpooling.services.VicinityService;
import iaws.carpooling.util.XmlBuilder;

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
	
	private static final String NAMESPACE_URI = "http://iaws/ws/contractfirst/carpooling";
	
	public VicinityEndpoint(VicinityService vicinityService) {
		this.vicinityService = vicinityService;
	}
	
	@PayloadRoot(localPart = "VicinityRequest", namespace = NAMESPACE_URI)
	@Namespace(prefix = "cov", uri = NAMESPACE_URI)
	@ResponsePayload
	public Element handleInscriptionRequest(@XPathParam("/cov:VicinityRequest/cov:Id") String id,
			@XPathParam("/cov:VicinityRequest/cov:Radius") int radius) throws Exception {
		
		ArrayList<Teacher> neighbors = vicinityService.getNeighbors(id, radius);
		
		return XmlBuilder.getElementFromNeighborsList(neighbors);		
	}

}
