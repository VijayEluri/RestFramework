package evs.rest.core.util;

import org.apache.log4j.Logger;

import evs.rest.core.RestServiceConfig;

public class RestUtil {
	private static Logger logger = Logger.getLogger(RestUtil.class);
	
	//Pattern patGetId = Pattern.compile("/[0-9]{1,}");
	//Matcher patGetMatcher = patGetId.matcher("");
	
	public static Integer getIdFromPath(String path) {
		/*patGetMatcher.reset(path);
		if(!patGetMatcher.find()) {
			logger.debug(path + "didn't match " + patGetMatcher.pattern().pattern());
			return -1;
		}*/
		Integer id;
		if(path != null && path.length() > 0) {
			try {
				return Integer.valueOf(path.substring(1));
			}
			catch(Exception e) {
			}
		}
		logger.debug("invalid id path: " + path);
		return -1;
	}

	/**
	 * sets the correct path for a service. eg: "rack"
	 * @param service the rest service class which the path is for
	 * @param path a custom path. if null, a default path-name will be generated from the service's class name. eg.: Class Rack would be "racks"
	 * @return the calculated path without leading "/", but ending with "/*"
	 */
	public static String servicePath(RestServiceConfig config, String path) {
		if(path == null)
			path = config.getEntityClass().getSimpleName().toLowerCase() + "s";
		
		if(!path.endsWith("/*") && path.endsWith("/"))
			path += "*";
		else
			path += "/*";
		
		if(path.startsWith("/"))
			path = path.substring(1);
		
		return path;
	}

}
