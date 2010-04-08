package evs.rest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import evs.rest.core.annotations.RestAcceptedFormats;
import evs.rest.core.annotations.RestEntity;
import evs.rest.core.annotations.RestId;
import evs.rest.core.annotations.RestPath;
import evs.rest.core.annotations.RestSearchIndexedFields;
import evs.rest.core.annotations.RestSearchPath;
import evs.rest.core.interceptors.RestInterceptor;
import evs.rest.core.marshal.RestFormat;
import evs.rest.core.persistence.RestPersistence;
import evs.rest.core.util.RestConst;
import evs.rest.core.util.RestUtil;


/**
 * configuration tasks for @RestServices are handled by this abstract configuration abstraction-layer
 */
public abstract class RestServiceConfig extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(RestServiceConfig.class);

	private static final long serialVersionUID = 1L;
	
	/* service options, configurable using annotations */
	
	/**
	 * the entity class, the implementing @RestService is configured for
	 */
	protected Class<Object> entityClass;
	
	/**
	 * the id class, the implementing @RestService is configured for
	 */
	protected Class<Object> idClass;
	
	/**
	 * the persistence layer, the implementing @RestService uses
	 */
	protected RestPersistence persistence;
	
	/**
	 * a list of @RestFormats, the implementing @RestService provides
	 */
	protected List<RestFormat> formats = new ArrayList<RestFormat>();
	
	/**
	 * the url path, the implementing @RestService is configured for
	 */
	protected String path;

	/**
	 * a list of full-text-searchable columns, the implementing @RestService provides
	 */
	protected List<String> searchIndexedFields = null;
	
	/**
	 * the search path, the implementing @RestService is configured for
	 */
	protected String searchPath = null;
	
	/**
	 * a list of @RestInterceptor instances, the implementing @RestService uses for logging
	 */
	protected List<RestInterceptor> interceptors = new ArrayList<RestInterceptor>();


	public RestServiceConfig() throws Exception {
		processRestEntityAnnotation();
		processRestIdAnnotation();
		processRestAcceptedFormatsAnnotation();
		processRestPathAnnotation();
		processRestSearchIndexedFieldsAnnotation();
		if(this.searchIndexedFields != null) {
			processRestSarchPathAnnotation();
		}
	}
	
	
	private void processRestEntityAnnotation() throws Exception {
		logger.debug("processing RestEntity annotation");
		RestEntity entityAnnotation = this.getClass().getAnnotation(RestEntity.class);
		if(entityAnnotation != null) {
			this.entityClass = (Class<Object>)entityAnnotation.value();
			logger.debug("entityClass = " + this.entityClass.getName());
		}
		else {
			Exception e = new Exception("invalid RestService given"); //TODO: exception handling
			logger.debug(e.getMessage(), e);
			throw e;
		}
	}


	private void processRestIdAnnotation() {
		logger.debug("processing Id annotation");
		RestId idAnnotation = this.getClass().getAnnotation(RestId.class);
		if(idAnnotation != null) {
			this.idClass = (Class<Object>)idAnnotation.value();
		}
		else {			
			this.idClass = RestConst.DEFAULT_ID_CLASS;
			logger.debug("no RestId provided, using default");
		}
		logger.debug("idClass = " + this.idClass.getName());
	}


	private void processRestAcceptedFormatsAnnotation() {
		logger.debug("processing RestAcceptedFormats annotation");
		RestAcceptedFormats formatsAnnotation = this.getClass().getAnnotation(RestAcceptedFormats.class);
		if(formatsAnnotation == null || formatsAnnotation.value() == null || formatsAnnotation.value().length == 0) {
			this.formats.addAll(Arrays.asList(RestConst.DEFAULT_FORMATS));
			logger.debug("no format provided, using defaults");
		}
		else {
			for(RestFormat format : formatsAnnotation.value()) {
				this.formats.add(format);
			}
		}

		for(RestFormat format : this.formats) {
			logger.debug("format: " + format);
		}
	}


	private void processRestPathAnnotation() {
		logger.debug("processing RestPath annotation");
		RestPath pathAnnotation = this.getClass().getAnnotation(RestPath.class);
		if(pathAnnotation == null) {
			this.path = RestUtil.defaultServicePath(this);	//will automatically set default path
			logger.debug("no path provided, using default: " + this.path);
		}
		else {
			this.path = RestUtil.servicePath(this, pathAnnotation.value());
			logger.debug("path set: " + this.path);
		}
	}


	private void processRestSearchIndexedFieldsAnnotation() {
		logger.debug("processing RestSearchIndexedFields annotation");
		RestSearchIndexedFields searchAnnotation = this.getClass().getAnnotation(RestSearchIndexedFields.class);
		if(searchAnnotation == null || searchAnnotation.value() == null || searchAnnotation.value().length == 0) {
			logger.debug("no search fields defined");
		}
		else {
			this.searchIndexedFields = new ArrayList<String>();
			for(String field : searchAnnotation.value()) {
				this.searchIndexedFields.add(field);
				logger.debug("added search field: " + field);
			}
		}
	}


	private void processRestSarchPathAnnotation() {
		logger.debug("processing RestSearchPath annotation");
		RestSearchPath searchPathAnnotation = this.getClass().getAnnotation(RestSearchPath.class);
		if(searchPathAnnotation == null) {
			this.setSearchPath(RestConst.DEFAULT_SEARCH_PATH);
			logger.debug("no search path provided, using default: " + this.searchPath);
		}
		else {
			this.setSearchPath(searchPathAnnotation.value());
			logger.debug("search path set: " + this.searchPath);
		}
	}

	public String getPath() {
		return this.path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public Class<Object> getEntityClass() {
		return this.entityClass;
	}

	public void setEntityClass(Class<Object> entityClass) {
		this.entityClass = entityClass;
	}

	public Class<Object> getIdClass() {
		return this.idClass;
	}

	public void setIdClass(Class<Object> idClass) {
		this.idClass = idClass;
	}

	public RestPersistence getPersistence() {
		return this.persistence;
	}

	public void setPersistence(RestPersistence persistence) {
		this.persistence = persistence;
	}

	public List<RestFormat> getFormats() {
		return this.formats;
	}

	public void setFormats(List<RestFormat> formats) {
		this.formats = formats;
	}
	
	public String getSearchPath() {
		return this.searchPath;
	}
	
	public void setSearchPath(String searchPath) {
		this.searchPath = searchPath;
	}
	
	public List<String> getSearchIndexedFields() {
		return searchIndexedFields;
	}

	public void setSearchIndexedFields(List<String> searchIndexedFields) {
		this.searchIndexedFields = searchIndexedFields;
	}


	public List<RestInterceptor> getInterceptors() {
		return interceptors;
	}


	public void setInterceptors(List<RestInterceptor> interceptors) {
		this.interceptors = interceptors;
	}

}
