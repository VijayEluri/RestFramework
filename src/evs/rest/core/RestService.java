package evs.rest.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import evs.rest.core.marshal.RestMarshaller;
import evs.rest.core.marshal.RestMarshallerFactory;
import evs.rest.core.persistence.RestPersistenceException;
import evs.rest.core.persistence.RestPersistenceValidationException;
import evs.rest.core.util.RestUtil;

public abstract class RestService extends RestServiceConfig {

	public RestService() throws Exception {
		super();
	}

	private static Logger logger = Logger.getLogger(RestService.class);

	private static final long serialVersionUID = 1L;

	/**
	 * handles the REST POST operation usage: POST http://SERVICE_URI/ creates a
	 * new object in the database.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("post received");

		RestMarshaller marshaller = RestMarshallerFactory
				.getMarshallerFromMimeCheck(req, this.formats);
		if (marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}

		// deserialize object
		Object object = RestUtil.unmarshalRequest(req, marshaller,
				this.entityClass);

		// store object
		Object result;
		try {
			result = this.persistence.create(object);
		} catch (RestPersistenceValidationException e) {
			validationErrors(e, resp);
			return;
		} catch (RestPersistenceException e) {
			exception(resp, e);
			return;
		}

		// return object
		resp.setStatus(HttpStatus.OK_200);
		RestUtil.marshalResponse(resp, marshaller, result);
	}

	/**
	 * handles the REST GET operation usage: GET http://SERVICE_URI/{id}
	 * retrieves an existing object by its id from the database
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("get received: " + req.getPathInfo());

		if (req.getPathInfo().endsWith(this.searchPath)) {
			doSearch(req, resp);
			return;
		}

		// process id
		Object id = RestUtil.getIdFromPath(req.getPathInfo(), this.idClass);

		// get object
		Object myObject = null;
		try {
			myObject = this.persistence.read(id, this.entityClass);
		} catch (RestPersistenceException e) {
			exception(resp, e);
			return;
		}

		// object not found
		if (myObject == null) {
			logger.debug("not found");
			resp.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}

		// check mime-type and formats. TODO: get-requests don't contain format
		logger.debug("mime-type: " + req.getContentType());
		RestMarshaller marshaller = RestMarshallerFactory
				.getMarshallerFromMimeCheck(req, this.formats);
		if (marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}

		// object found
		resp.setStatus(HttpStatus.OK_200);
		RestUtil.marshalResponse(resp, marshaller, myObject);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("put received");

		RestMarshaller marshaller = RestMarshallerFactory
				.getMarshallerFromMimeCheck(req, this.formats);
		if (marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}

		// deserialize object
		Object object = RestUtil.unmarshalRequest(req, marshaller,
				this.entityClass);

		// update object
		Object result;
		try {
			result = this.persistence.update(object);
		} catch (RestPersistenceValidationException e) {
			validationErrors(e, resp);
			return;
		} catch (RestPersistenceException e) {
			exception(resp, e);
			return;
		}

		// return object
		resp.setStatus(HttpStatus.OK_200);
		RestUtil.marshalResponse(resp, marshaller, result);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("delete received: " + req.getPathInfo());

		// process id
		Object id = RestUtil.getIdFromPath(req.getPathInfo(), this.idClass);

		// delete
		try {
			if (this.persistence.delete(id, this.entityClass)) {
				resp.setStatus(HttpStatus.OK_200); // OK
			} else {
				resp.setStatus(HttpStatus.BAD_REQUEST_400); // delete failed
			}
		} catch (RestPersistenceException e) {
			exception(resp, e);
			return;
		}
	}

	private void doSearch(HttpServletRequest req, HttpServletResponse resp) {
		//process search parameters
		String text = req.getParameter("text");
		String[] f = req.getParameterValues("fields");
		List<String> fields = f != null ? Arrays.asList(f) : this.searchIndexedFields;
		
		//search
		List<Object> result;
		try {
			result = this.persistence.search(text, this.entityClass, fields);
		} catch (RestPersistenceException e) {
			exception(resp, e);
			return;
		}

		//get marshaller
		RestMarshaller marshaller = RestMarshallerFactory
				.getMarshallerFromMimeCheck(req, this.formats);
		if (marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}

		//return results
		resp.setStatus(HttpStatus.OK_200);
		RestUtil.marshalResponse(resp, marshaller, result);

	}

	private void validationErrors(RestPersistenceValidationException e,
			HttpServletResponse resp) {
		resp.setStatus(HttpStatus.BAD_REQUEST_400);
		try {
			PrintWriter writer = resp.getWriter();
			writer.write("validation errors:\n");
			for(ConstraintViolation<Object> violation : e.getViolations()) {
				writer.write(violation.getMessage() + "\n");
			}
		} catch (IOException e2) {
			exception(resp, e2);
		}
	}

	private void exception(HttpServletResponse resp, Exception e) {
		resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
		try {
			resp.getWriter().write("Internal Server Error");
		} catch (IOException e1) {
			logger.warn(e.getMessage(), e);
		}
	}

	/*
	 * doHead doOptions service?
	 * 
	 * Called by the server (via the service method) to allow a servlet to
	 * handle a GET request. protected void doHead(HttpServletRequest req,
	 * HttpServletResponse resp) Receives an HTTP HEAD request from the
	 * protected service method and handles the request. protected void
	 * doOptions(HttpServletRequest req, HttpServletResponse resp) Called by the
	 * server (via the service method) to allow a servlet to handle a OPTIONS
	 * request.
	 */
}
