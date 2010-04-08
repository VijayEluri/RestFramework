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

import evs.rest.core.interceptors.RestInterceptor;
import evs.rest.core.marshal.RestMarshaller;
import evs.rest.core.marshal.RestMarshallerException;
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
	 * 
	 * responses
	 * - @HttpStatus.OK_200 on success. the saved object will be returned as encoded entity in the same format as the request was by it's mimeType definition
	 * - @HttpStatus.UNSUPPORTED_MEDIA_TYPE_415 if request mimeType can't be mapped to a @RestMarshaller instance or this particular service doesn't support it
	 * - @HttpStatus.BAD_REQUEST_400 for validation errors. the response will textually describe all validation errors found
	 * - @HttpStatus.INTERNAL_SERVER_ERROR_500 for any other error on service or database level. there won't be any details on the error in the response for security reasons
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("post received");

		for (RestInterceptor interceptor : this.interceptors) {
			interceptor.beforeServicePost(req, resp);
		}

		RestMarshaller marshaller = RestUtil.getMarshallerFromMimeCheck(req, this.formats);
		if (marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}

		// deserialize object
		Object object;
		try {
			object = unmarshalRequest(req, marshaller, this.entityClass);
		} catch (RestMarshallerException e) {
			exception(resp, e);
			return;
		} catch (IOException e) {
			exception(resp, e);
			return;
		}

		// store object
		Object result;
		try {
			for (RestInterceptor interceptor : this.interceptors) {
				interceptor.beforeDBCreate(object);
			}
			result = this.persistence.create(object);
			for (RestInterceptor interceptor : this.interceptors) {
				interceptor.afterDBCreate(object);
			}
		} catch (RestPersistenceValidationException e) {
			validationErrors(e, resp);
			return;
		} catch (RestPersistenceException e) {
			exception(resp, e);
			return;
		}

		// return object
		resp.setStatus(HttpStatus.OK_200);
		try {
			marshalResponse(resp, marshaller, result);
		} catch (RestMarshallerException e) {
			exception(resp, e);
			return;
		} catch (IOException e) {
			exception(resp, e);
			return;
		}

		for (RestInterceptor interceptor : this.interceptors) {
			interceptor.afterServicePost(req, resp);
		}
	}

	/**
	 * handles the REST GET operation usage: GET http://SERVICE_URI/{id}
	 * retrieves an existing object by its id from the database. search requests are forwarded to doSearch
	 * 
	 * responses
	 * - @HttpStatus.OK_200 on success. the retrieved object will be returned as encoded entity in the same format as the request was by it's mimeType definition
	 * - @HttpStatus.NOT_FOUND_404 if the object could not be found
	 * - @HttpStatus.UNSUPPORTED_MEDIA_TYPE_415 if request mimeType can't be mapped to a @RestMarshaller instance or this particular service doesn't support it
	 * - @HttpStatus.INTERNAL_SERVER_ERROR_500 for any other error on service or database level. there won't be any details on the error in the response for security reasons
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("get received: " + req.getPathInfo());

		for (RestInterceptor interceptor : this.interceptors) {
			interceptor.beforeServiceGet(req, resp);
		}

		if (this.searchPath != null && req.getPathInfo().endsWith(this.searchPath)) {
			doSearch(req, resp);
			return;
		}

		// process id
		Object id = RestUtil.getIdFromPath(req.getPathInfo(), this.idClass);

		// get object
		Object myObject = null;
		try {
			for (RestInterceptor interceptor : this.interceptors) {
				interceptor.beforeDBRead(id);
			}
			myObject = this.persistence.read(id, this.entityClass);
			for (RestInterceptor interceptor : this.interceptors) {
				interceptor.afterDBRead(id);
			}
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
		RestMarshaller marshaller = RestUtil.getMarshallerFromMimeCheck(req, this.formats);
		if (marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}

		// object found
		resp.setStatus(HttpStatus.OK_200);
		try {
			marshalResponse(resp, marshaller, myObject);
		} catch (RestMarshallerException e) {
			exception(resp, e);
			return;
		} catch (IOException e) {
			exception(resp, e);
			return;
		}

		for (RestInterceptor interceptor : this.interceptors) {
			interceptor.afterServiceGet(req, resp);
		}
	}

	/**
	 * handles the REST PUT operation usage: PUT http://SERVICE_URI/ updates an
	 * existing object within the database
	 * 
	 * responses
	 * - @HttpStatus.OK_200 on success. the updated object will be returned as encoded entity in the same format as the request was by it's mimeType definition
	 * - @HttpStatus.UNSUPPORTED_MEDIA_TYPE_415 if request mimeType can't be mapped to a @RestMarshaller instance or this particular service doesn't support it
	 * - @HttpStatus.INTERNAL_SERVER_ERROR_500 for any other error on service or database level. there won't be any details on the error in the response for security reasons
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("put received");

		for (RestInterceptor interceptor : this.interceptors) {
			interceptor.beforeServicePut(req, resp);
		}

		RestMarshaller marshaller = RestUtil.getMarshallerFromMimeCheck(req, this.formats);
		if (marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}

		// deserialize object
		Object object;
		try {
			object = unmarshalRequest(req, marshaller, this.entityClass);
		} catch (RestMarshallerException e) {
			exception(resp, e);
			return;
		} catch (IOException e) {
			exception(resp, e);
			return;
		}

		// update object
		Object result;
		try {
			for (RestInterceptor interceptor : this.interceptors) {
				interceptor.beforeDBUpdate(object);
			}

			result = this.persistence.update(object);

			for (RestInterceptor interceptor : this.interceptors) {
				interceptor.afterDBUpdate(object);
			}
		} catch (RestPersistenceValidationException e) {
			validationErrors(e, resp);
			return;
		} catch (RestPersistenceException e) {
			exception(resp, e);
			return;
		}

		// return object
		resp.setStatus(HttpStatus.OK_200);
		try {
			marshalResponse(resp, marshaller, result);
		} catch (RestMarshallerException e) {
			exception(resp, e);
			return;
		} catch (IOException e) {
			exception(resp, e);
			return;
		}

		for (RestInterceptor interceptor : this.interceptors) {
			interceptor.afterServicePut(req, resp);
		}
	}

	/**
	 * handles the REST DELETE operation usage: PUT http://SERVICE_URI/{id}
	 * deletes an existing object from the database
	 * 
	 * responses
	 * - @HttpStatus.OK_200 on success.
     * - @HttpStatus.NOT_FOUND_404 if the object could not be found
	 * - @HttpStatus.INTERNAL_SERVER_ERROR_500 for any other error on service or database level. there won't be any details on the error in the response for security reasons
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("delete received: " + req.getPathInfo());

		for (RestInterceptor interceptor : this.interceptors) {
			interceptor.beforeServiceDelete(req, resp);
		}

		// process id
		Object id = RestUtil.getIdFromPath(req.getPathInfo(), this.idClass);

		// delete
		try {
			for (RestInterceptor interceptor : this.interceptors) {
				interceptor.beforeDBDelete(id);
			}
			if (this.persistence.delete(id, this.entityClass)) {
				for (RestInterceptor interceptor : this.interceptors) {
					interceptor.afterDBDelete(id);
				}
				resp.setStatus(HttpStatus.OK_200); // OK
			} else {
				resp.setStatus(HttpStatus.NOT_FOUND_404); // delete failed
			}
		} catch (RestPersistenceException e) {
			exception(resp, e);
			return;
		}
	}

	/**
	 * handles the search operation usage: GET http://SERVICE_URI/search?text=SEARCH_TEXT&FIELDS=FIELDA&FIELDS=FIELDB
	 * deletes an existing object from the database
	 * 
	 * responses
	 * - @HttpStatus.OK_200 on success. a list of found objects will be returned as encoded entity in the same format as the request was by it's mimeType definition
     * - @HttpStatus.UNSUPPORTED_MEDIA_TYPE_415 if request mimeType can't be mapped to a @RestMarshaller instance or this particular service doesn't support it
	 * - @HttpStatus.INTERNAL_SERVER_ERROR_500 for any other error on service or database level. there won't be any details on the error in the response for security reasons
	 */
	private void doSearch(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("search request received");

		for (RestInterceptor interceptor : this.interceptors) {
			interceptor.beforeServiceSearch(req, resp);
		}

		// process search parameters
		String text = req.getParameter("text");
		String[] f = req.getParameterValues("fields");
		List<String> fields = f != null ? Arrays.asList(f) : this.searchIndexedFields;

		// search
		List<Object> result;
		try {
			for (RestInterceptor interceptor : this.interceptors) {
				interceptor.beforeDBSearch(text, this.entityClass, fields);
			}
			result = this.persistence.search(text, this.entityClass, fields);
			for (RestInterceptor interceptor : this.interceptors) {
				interceptor.afterDBSearch(result);
			}
		} catch (RestPersistenceException e) {
			exception(resp, e);
			return;
		}

		// get marshaller
		RestMarshaller marshaller = RestUtil.getMarshallerFromMimeCheck(req, this.formats);
		if (marshaller == null) {
			logger.debug("format unsupported");
			resp.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE_415);
			return;
		}

		// return results
		resp.setStatus(HttpStatus.OK_200);
		try {
			marshalResponse(resp, marshaller, result);
		} catch (RestMarshallerException e) {
			exception(resp, e);
			return;
		} catch (IOException e) {
			exception(resp, e);
			return;
		}

		for (RestInterceptor interceptor : this.interceptors) {
			interceptor.afterServiceSearch(req, resp);
		}
	}

	private void validationErrors(RestPersistenceValidationException e, HttpServletResponse resp) {
		resp.setStatus(HttpStatus.BAD_REQUEST_400);
		try {
			PrintWriter writer = resp.getWriter();
			writer.write("validation errors:\n");
			for (ConstraintViolation<Object> violation : e.getViolations()) {
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

	/**
	 * reads an object from a given request by using the marshaller provided
	 * 
	 * @param req
	 *            the request object, to retrieve the @InputStream from
	 * @param marshaller
	 *            the marshaller, to unserialize the object
	 * @param clazz
	 *            the object class
	 * @return the object on success, null on error
	 * @throws IOException
	 * @throws RestMarshallerException
	 */
	private <T> T unmarshalRequest(HttpServletRequest req, RestMarshaller marshaller, Class<T> clazz) throws RestMarshallerException,
			IOException {

		return marshaller.read(clazz, req.getInputStream());
	}

	/**
	 * responds by marshaling a given object
	 * 
	 * @param resp
	 *            servlet response which holds the @OutputStream
	 * @param marshaller
	 *            marshaller to serialize the object
	 * @param myObject
	 *            the object to marshal
	 */
	private <T> void marshalResponse(HttpServletResponse resp, RestMarshaller marshaller, T myObject) throws RestMarshallerException,
			IOException {
		marshaller.write(myObject, resp.getOutputStream());
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
