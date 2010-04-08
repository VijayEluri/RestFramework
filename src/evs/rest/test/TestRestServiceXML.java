package evs.rest.test;

import org.junit.BeforeClass;

import evs.rest.core.marshal.XStreamXMLMarshaller;

public class TestRestServiceXML extends TestRestService {

	@BeforeClass
	public static void beforePrepareClass() throws Exception  {
		marshaller = new XStreamXMLMarshaller();
		mimeType = "application/xml";
		serverPort = 8777;
		TestRestService.prepareClass();
	} 
}
