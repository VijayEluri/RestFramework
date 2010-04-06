package evs.rest.core.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import evs.rest.core.RestServiceConfig;
import evs.rest.core.marshal.RestMarshaller;
import evs.rest.core.marshal.RestMarshallerException;

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
	 * reads an object from a given request by using the marshaller provided
	 * @param req the request object, to retrieve the @InputStream from
	 * @param marshaller the marshaller, to unserialize the object
	 * @param clazz the object class
	 * @return the object on success, null on error
	 */
	public static <T> T unmarshalRequest(HttpServletRequest req,
			RestMarshaller marshaller, Class<T> clazz) {

		try {
			return marshaller.read(clazz, req.getInputStream());
		} catch (RestMarshallerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * responds by marshaling a given object
	 * @param resp servlet response which holds the @OutputStream
	 * @param marshaller marshaller to serialize the object
	 * @param myObject the object to marshal
	 */
	public static void marshalResponse(HttpServletResponse resp,
			RestMarshaller marshaller, Object myObject) {
		try {
			marshaller.write(myObject, resp.getOutputStream());
		} catch (RestMarshallerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
