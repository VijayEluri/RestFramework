package evs.rest.test;

import evs.rest.core.RestServiceProvider;
import evs.rest.test.RackService;

public class RackTest {
	
	public static void main(String[] args) {
		RestServiceProvider provider = new RestServiceProvider();
		provider.addService(RackService.class);
		provider.setPath("localhost");

		provider.start();
		
	}

}
