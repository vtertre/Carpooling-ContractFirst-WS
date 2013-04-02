package iaws.carpooling.services.impl;

import java.util.ArrayList;

import iaws.carpooling.domain.Teacher;
import iaws.carpooling.services.VicinityService;
import junit.framework.TestCase;

public class TestVicinityServiceImpl extends TestCase {
	
	public void testGetNeighbors() throws Exception {
		VicinityService sv = new VicinityServiceImpl();
		ArrayList<Teacher> al = sv.getNeighbors("toto", 5);
		
		assert al != null;
	}

}
