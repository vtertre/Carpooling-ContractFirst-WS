package iaws.carpooling;

import iaws.carpooling.util.TestXmlBuilder;
import iaws.carpooling.ws.contractfirst.IntegrationTestInscriptionEndpoint;
import iaws.carpooling.ws.contractfirst.IntegrationTestVicinityEndpoint;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = { TestXmlBuilder.class,
		IntegrationTestInscriptionEndpoint.class,
		IntegrationTestVicinityEndpoint.class})
public class AllTests {
}