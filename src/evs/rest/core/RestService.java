package evs.rest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
	 * handles the REST GET operation usage: GET http://SERVICE_URI/{id}
	 * retrieves an existing object by its id from the database
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("get received: " + req.getPathInfo());
		
		if(req.getPathInfo().endsWith(this.searchPath)) {
			doSearch(req, resp);
			return;
		}

		// process id
		Object id = RestUtil.getIdFromPath(req.getPathInfo(), this.idClass);

		// get object
		Object myObject = this.persistence.read(id, this.entityClass); // getObjectFromDB

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
		resp.setStatus(200); // TODO: HTTP.status.OK
		RestUtil.marshalResponse(resp, marshaller, myObject);
	}

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
		}

		// return object
		resp.setStatus(HttpStatus.OK_200);
		RestUtil.marshalResponse(resp, marshaller, result);
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

		if (this.persistence.delete(id, this.entityClass)) {
			// OK
			resp.setStatus(HttpStatus.OK_200);
		} else {
			// TODO: delete failed
			resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
	}
	
	private void doSearch(HttpServletRequest req, HttpServletResponse resp) {
		String text = req.getParameter("text");
		String[] f = req.getParameterValues("fields");
		List<String> fields = f != null ? Arrays.asList(f) : this.searchIndexedFields;
		List<Object> result = this.persistence.search(text, this.entityClass, fields);
		
		RestMarshaller marshaller = RestMarshallerFactory.getMarshallerFromMimeCheck(req, this.formats);
		if (marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}

		resp.setStatus(HttpStatus.OK_200);
		RestUtil.marshalResponse(resp, marshaller, result);
		
	}

	private void validationErrors(RestPersistenceValidationException e,
			HttpServletResponse resp) {
		logger.debug("validation errors");
		resp.setStatus(HttpStatus.BAD_REQUEST_400);
		// TODO: write validation errors
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
