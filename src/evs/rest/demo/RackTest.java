package evs.rest.demo;

import java.io.OutputStream;

import evs.rest.core.RestProvider;
import evs.rest.core.marshal.JSONMarshaller;
import evs.rest.core.marshal.RestMarshaller;
import evs.rest.demo.domain.Rack;

public class RackTest {
	
	public static void main(String[] args) throws Exception {
		RestProvider provider = new RestProvider();
		RackService service = new RackService();
		provider.addService(service);
		provider.setServerPath("/");

		provider.start();
		
		Rack rack = new Rack();
		rack.setName("Rack1");
		rack.setPlace(1);
		
		RestMarshaller marshaller = new JSONMarshaller();
		marshaller.write(rack, System.out);
		
		
	}

}
