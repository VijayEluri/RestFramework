package evs.rest.core.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import evs.rest.core.RestServiceConfig;
import evs.rest.core.marshal.RestFormat;
import evs.rest.core.marshal.UnknownRestFormatException;
import evs.rest.core.marshal.RestMarshaller;
import evs.rest.core.marshal.RestMarshallerException;
import evs.rest.core.marshal.RestMarshallerFactory;

public class RestUtil {
	private static Logger logger = Logger.getLogger(RestUtil.class);
	
	//Pattern patGetId = Pattern.compile("/[0-9]{1,}");
	//Matcher patGetMatcher = patGetId.matcher("");
	
	public static Object getIdFromPath(String path, Class<Object> idClass) {
		
		if(!idClass.equals(Long.class)) {
			//TODO: id type conversion
			logger.warn("not implemented idClass");
		}
		
		/*patGetMatcher.reset(path);
		if(!patGetMatcher.find()) {
			logger.debug(path + "didn't match " + patGetMatcher.pattern().pattern());
			return -1;
		}*/
		if(path != null && path.length() > 0) {
			try {
				String id = path.substring(1);
				logger.debug("object id: " + id);
				return Long.valueOf(id);
			}
			catch(Exception e) {
			}
		}
		logger.debug("invalid id path: " + path);
		return Long.valueOf(-1);	//TODO: id from path exception handling
	}

	/**
	 * sets the correct path for a service. eg: "rack"
	 * @param service the rest service class which the path is for
	 * @param path a custom path. if null, a default path-name will be generated from the service's class name. eg.: Class Rack would be "racks"
	 * @return the calculated path without leading "/"
	 */
	public static String servicePath(RestServiceConfig config, String path) {
		if(path == null)
			path = config.getEntityClass().getSimpleName().toLowerCase() + "s";
		
		/*
		if(!path.endsWith("/*") && path.endsWith("/"))
			path += "*";
		else
			path += "/*";
		*/
		
		if(path.startsWith("/"))
			path = path.substring(1);
		
		return path;
	}

	/**
	 * a default path-name will be generated from the service's class name. eg.: Class Rack would be "racks"
	 * @param service the rest service class which the path is for
	 * @return the calculated path without leading "/"
	 */
	public static String defaultServicePath(RestServiceConfig config) {
		return servicePath(config, null);
	}
	
	

	/**
	 * provides a marshaller instance by comparing a @HttpServletRequest mime-type with a list of supported @RestFormat formats
	 * @param req the request specifying the mime-type
	 * @param formats a list of supported formats
	 * @return the marshaller on success, else null 
	 */
	public static RestMarshaller getMarshallerFromMimeCheck(
			HttpServletRequest req, List<RestFormat> formats) {
		String mimeType = req.getContentType();
		logger.debug("determining marshaller for request format mimeType: " + mimeType);
		try {
			if(mimeType != null) {
				//format specified
				RestFormat requestFormat = RestFormat.fromMimeType(mimeType);
				if(formats.contains(requestFormat)) {
					return RestMarshallerFactory.getMarshaller(requestFormat);
				}
				//TODO: requested format not supported by service
				return null;
			}
			else {
				logger.debug("no format specified, using default formatter");
				return RestMarshallerFactory.getMarshaller(formats.get(0));
			}
		} catch (UnknownRestFormatException e) {
			//TODO: requested format unknown
			return null;
		}
	}


}
