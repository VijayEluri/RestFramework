package evs.rest.demo;


import evs.rest.core.RestServer;
import evs.rest.core.persistence.HibernatePersistence;
import evs.rest.core.persistence.RestPersistence;

public class RackTest {
	
	public static void main(String[] args) throws Exception {
		RestPersistence pers = HibernatePersistence.getInstance();
		/*
		RestServer provider = new RestServer();
		RackService service = new RackService();
		provider.addService(service);
		provider.setServerPath("/");

		provider.start();
		*/
		
	}

}
