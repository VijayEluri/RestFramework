package evs.rest.core;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import evs.rest.core.annotations.RestAcceptedFormats;
import evs.rest.core.annotations.RestEntity;
import evs.rest.core.annotations.RestFormat;
import evs.rest.core.annotations.RestId;
import evs.rest.core.annotations.RestPath;
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
	
	public RestServiceConfig() throws Exception {

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
		

		logger.debug("processing Id annotation");
		RestId idAnnotation = this.getClass().getAnnotation(RestId.class);
		if(idAnnotation != null) {
			this.idClass = (Class<Object>)idAnnotation.value();
		}
		else {			
			this.idClass = RestConst.DEFAULT_ID_CLASS;
			logger.debug("no RestId given, using default");
		}
		logger.debug("idClass = " + this.idClass.getName());

		
		logger.debug("processing RestAcceptedFormats annotation");
		RestAcceptedFormats formatsAnnotation = this.getClass().getAnnotation(RestAcceptedFormats.class);
		if(formatsAnnotation == null || formatsAnnotation.value() == null) {
			this.formats.add(RestConst.DEFAULT_FORMAT);
			logger.debug("no format given, using default: " + RestConst.DEFAULT_FORMAT);
		}
		else {
			for(RestFormat format : formatsAnnotation.value()) {
				this.formats.add(format);
				logger.debug("added format: " + format);
			}
		}
		
		logger.debug("processing RestPath annotation");
		RestPath pathAnnotation = this.getClass().getAnnotation(RestPath.class);
		if(pathAnnotation == null) {
			this.setPath(null);
			logger.debug("no path given, using default: " + this.path);
		}
		else {
			this.setPath(pathAnnotation.value());
			logger.debug("path set: " + this.path);
		}
		
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void setPath(String path) {
		this.path = RestUtil.servicePath(this, path);
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

}
