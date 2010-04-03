package evs.rest.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import evs.rest.core.marshal.RestMarshaller;
import evs.rest.core.marshal.RestMarshallerFactory;
import evs.rest.core.persistence.RestPersistenceValidationException;
import evs.rest.core.util.RestUtil;

public abstract class RestService extends RestServiceConfig {

	public RestService() throws Exception {
		super();
	}

	private static Logger logger = Logger.getLogger(RestService.class);

	private static final long serialVersionUID = 1L;
	

	/**
	 * handles the REST GET operation
	 * usage: GET http://SERVICE_URI/{id}
	 * retrieves an existing object by its id from the database
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("get received: " + req.getPathInfo());
		
		//check mime-type and formats. TODO: get-requests don't contain format
		logger.debug("mime-type: " + req.getContentType());
		RestMarshaller marshaller = RestMarshallerFactory.getMarshallerFromMimeCheck(req, this.formats);
		if(marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}
	
		//process id
		Long id = RestUtil.getIdFromPath(req.getPathInfo());
		if(!this.idClass.equals(Long.class)) {
			//TODO: id type conversion
			logger.warn("not implemented idClass");
		}
		logger.debug("object id: " + id);
		
		//get object
		Object myObject = this.persistence.read(id, this.entityClass); //getObjectFromDB
		
		//object not found
		if(myObject == null) {
			logger.debug("not found");
			resp.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
				
		//object found
		resp.setStatus(200); //TODO: HTTP.status.OK
		RestUtil.marshalResponse(resp, marshaller, myObject);
	}

	/**
	 * handles the REST POST operation
	 * usage: POST http://SERVICE_URI/
	 * creates a new object in the database.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("post received");
		
		RestMarshaller marshaller = RestMarshallerFactory.getMarshallerFromMimeCheck(req, this.formats);
		if(marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}

		//deserialize object
		Object object;
		object = RestUtil.unmarshalRequest(req, marshaller, this.entityClass);

		//store object
		Object result;
		try {
			result = this.persistence.create(object);
		} catch (RestPersistenceValidationException e) {
			logger.debug("validation errors");
			resp.setStatus(HttpStatus.BAD_REQUEST_400);
			//TODO: write validation errors
			return;
		}
		
		//return object
		resp.setStatus(HttpStatus.OK_200);
		RestUtil.marshalResponse(resp, marshaller, result);
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
