package evs.rest.core;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class RestServiceProvider {

	public void addService(Class clazz) {
		// TODO Auto-generated method stub
		
	}

	public void setPath(String string) {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		
        try {

    		Server server = new Server(8777);
    		
        	ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
     
            context.addServlet(new ServletHolder(new RestService()),"/*");
        	
			server.start();
	        server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}}
