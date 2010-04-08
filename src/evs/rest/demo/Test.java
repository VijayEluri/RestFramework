package evs.rest.demo;

import evs.rest.core.RestServer;
import evs.rest.core.interceptors.BasicInterceptor;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		RestServer provider = new RestServer();
		
		provider.addInterceptor(new BasicInterceptor());
		
		provider.addService(new RackService());
		provider.addService(new ItemService());
		provider.addService(new PlacementService());
		
		provider.start();
	}

}
