package evs.rest.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import evs.rest.core.persistence.HibernatePersistence;
import evs.rest.core.persistence.RestPersistence;
import evs.rest.core.util.RestConst;


public class RestServer {
	
	/* logger */
	private static Logger logger = Logger.getLogger(RestServer.class);
	
	private List<RestService> services = new ArrayList<RestService>();
	private String serverPath = RestConst.DEFAULT_SERVICE_PATH;
	private RestPersistence persistence;
	private int serverPort = RestConst.DEFAULT_SERVER_PORT;
	
	public void addService(RestService service) {
		this.services.add(service);
	}

	public void setServerPath(String path) {
		this.serverPath = path;
	}
	
	public void setPersistance(RestPersistence persistance) {
		this.persistence = persistance;
	}
	public void setServerPort(int port) {
		this.serverPort = port;
	}

	public void start() {
		if(persistence == null) {
			logger.debug("no persistence configured, using Hibernate as default");
			this.persistence = HibernatePersistence.getInstance();
		}
		
        try {

    		Server server = new Server(this.serverPort);
    		
        	ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath(this.serverPath);
            server.setHandler(context);
            
            for(RestService service : this.services) {
            	logger.debug("adding service: " + service.getClass().getName());
            	
        		String publishPath = this.serverPath + service.getPath() + "/*";
        		logger.debug("publish path: " + publishPath);
        		
        		service.setPersistence(this.persistence);
        		service.init();
        		
                context.addServlet(new ServletHolder(service),publishPath);
                logger.debug("service added");
                
            }
            
        	
			server.start();
	        //server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}}