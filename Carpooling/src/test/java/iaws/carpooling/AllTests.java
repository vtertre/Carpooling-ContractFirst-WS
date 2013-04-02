package iaws.carpooling;

import iaws.carpooling.services.impl.TestInscriptionServiceImpl;
import iaws.carpooling.services.impl.TestVicinityServiceImpl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = {TestInscriptionServiceImpl.class,
		TestVicinityServiceImpl.class})
public class AllTests {
}