package evs.rest.test;

import org.junit.BeforeClass;

import evs.rest.core.marshal.XStreamJSONMarshaller;

public class TestRestServiceJSON extends TestRestService {

	@BeforeClass
	public static void beforePrepareClass() throws Exception  {
		marshaller = new XStreamJSONMarshaller();
		mimeType = "application/json";
		serverPort = 8787;
		TestRestService.prepareClass();
	} 
}
