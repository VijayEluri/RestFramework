package evs.rest.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import evs.rest.core.persistance.HibernatePersistance;
import evs.rest.core.persistance.RestPersistance;
import evs.rest.core.util.RestConst;


public class RestProvider {
	
	/* logger */
	private static Logger logger = Logger.getLogger(RestProvider.class);
	
	private List<RestService> services = new ArrayList<RestService>();
	private String serverPath = RestConst.DEFAULT_SERVICE_PATH;
	private RestPersistance persistance;
	
	public void addService(RestService service) {
		this.services.add(service);
	}

	public void setServerPath(String path) {
		this.serverPath = path;
	}
	
	public void setPersistance(RestPersistance persistance) {
		this.persistance = persistance;
	}

	public void start() {
		if(persistance == null) {
			logger.debug("no persistance configured, using Hibernate as default");
			this.persistance = new HibernatePersistance();
		}
		
        try {

    		Server server = new Server(8777);
    		
        	ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath(this.serverPath);
            server.setHandler(context);
            
            for(RestService service : this.services) {
            	logger.debug("adding service: " + service.getClass().getName());
            	
        		String publishPath = this.serverPath + service.getPath();
        		logger.debug("publish path: " + publishPath);
        		
        		service.setPersistance(this.persistance);
        		
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