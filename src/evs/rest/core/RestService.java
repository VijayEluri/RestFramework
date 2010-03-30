package evs.rest.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import evs.rest.core.marshal.JSONMarshaller;
import evs.rest.core.marshal.RestMarshaller;
import evs.rest.core.marshal.RestMarshallerException;
import evs.rest.core.util.RestUtil;

public class RestService extends RestServiceConfig {

	public RestService() throws Exception {
		super();
	}

	private static Logger logger = Logger.getLogger(RestService.class);

	private static final long serialVersionUID = 1L;
	
	
	@Override
	/**
	 * handles the REST GET operation
	 * 
	 * usage: "http://SERVICE_URI/{id}" retrieves an existing object from the database
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("get received: " + req.getPathInfo());
		
		Integer id = RestUtil.getIdFromPath(req.getPathInfo());
		logger.debug("object id: " + id);
		
		Object myObject = this.persistance.read(id); //getObjectFromDB
		
		if(myObject == null) {
			resp.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		
				
		resp.setStatus(200); //TODO: HTTP.status.OK
		try {
			resp.getWriter().print("ok");  //TODO: write object JSON/XML
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("post received");
		
		RestMarshaller marshaller = new JSONMarshaller();

		try {
			//deserialize object
			Object object = marshaller.read(this.entityClass, req.getInputStream());
			
			//store object
			Object result = this.persistance.create(object);
			
			//response
			resp.setStatus(HttpStatus.OK_200);
			marshaller.write(result, resp.getOutputStream());
			
			
		} catch (RestMarshallerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			resp.getWriter().print("saved");  //TODO: write object JSON/XML
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("put received");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("delete received");
	}
	
	
	/*
	 * doHead
	 * doOptions
	 * service?

    Called by the server (via the service method) to allow a servlet to handle a GET request.
protected  void	doHead(HttpServletRequest req, HttpServletResponse resp) 
    Receives an HTTP HEAD request from the protected service method and handles the request.
protected  void	doOptions(HttpServletRequest req, HttpServletResponse resp) 
    Called by the server (via the service method) to allow a servlet to handle a OPTIONS request.
*/
}
