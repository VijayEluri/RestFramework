package evs.rest.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import evs.rest.core.interceptors.RestInterceptor;
import evs.rest.core.persistence.HibernatePersistence;
import evs.rest.core.persistence.RestPersistence;
import evs.rest.core.util.RestConst;


public class RestServer {
	
	/* logger */
	private static Logger logger = Logger.getLogger(RestServer.class);
	
	private List<RestService> services = new ArrayList<RestService>();
	private String serverPath = RestConst.DEFAULT_SERVER_PATH;
	private RestPersistence persistence;
	private int serverPort = RestConst.DEFAULT_SERVER_PORT;
	private List<RestInterceptor> interceptors = new ArrayList<RestInterceptor>();
	
	/**
	 * registeres a @RestService with the server
	 * @param service service instance
	 */
	public void addService(RestService service) {
		this.services.add(service);
	}

	/**
	 * sets the server path. by default, RestServer will use @RestConst.DEFAULT_SERVICE_PATH
	 * @param path
	 */
	public void setServerPath(String path) {
		this.serverPath = path;
	}
	
	/**
	 * sets the server port. by default, RestServer will use @RestConst.DEFAULT_SERVER_PORT
	 * @param port
	 */
	public void setServerPort(int port) {
		this.serverPort = port;
	}
	
	/**
	 * sets the persistence unit, which the services of this RestServer will use
	 * @param persistance
	 */
	public void setPersistance(RestPersistence persistance) {
		this.persistence = persistance;
	}
	
	/**
	 * registers interceptors for the services
	 * @param interceptor
	 */
	public void addInterceptor(RestInterceptor interceptor) {
		this.interceptors.add(interceptor);
	}

	/**
	 * starts the rest-server
	 */
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
            	
        		String publishPath = "/" + service.getPath() + "/*";
        		logger.debug("publish path: " + publishPath);
        		
        		service.setPersistence(this.persistence);
        		
        		//register configured interceptors
        		service.getInterceptors().addAll(this.interceptors);
        		
        		service.init();
        		
                context.addServlet(new ServletHolder(service),publishPath);
                logger.debug("service added");
                
            }
            
        	
			server.start();
			
			logger.debug("server started");
			logger.debug("server path: " + this.serverPath);
			logger.debug("server port: " + this.serverPort);
	        //server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}}