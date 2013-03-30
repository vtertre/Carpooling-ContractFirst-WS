package iaws.covoiturage.services.impl;

import java.util.ArrayList;

import iaws.covoiturage.domain.Teacher;
import iaws.covoiturage.services.VicinityService;
import junit.framework.TestCase;

public class TestVicinityServiceImpl extends TestCase {
	
	public void testGetNeighbors() throws Exception {
		VicinityService sv = new VicinityServiceImpl();
		ArrayList<Teacher> al = sv.getNeighbors("toto", 5);
		
		assert al.size() > 0;
	}

}
