package evs.rest.core.annotations;

import org.apache.log4j.Logger;

import evs.rest.core.RestService;
import evs.rest.core.util.RestConst;

public enum RestFormat {
	
	JSON, XML;
	
	private static Logger logger = Logger.getLogger(RestFormat.class);
	
	public static RestFormat fromMimeType(String mimeType) throws RestFormatException {
		//if(mimeType == null)
		//	return RestConst.DEFAULT_FORMAT;
		
		if(mimeType.startsWith("application/json")
				|| mimeType.startsWith("text/json")) {
			return JSON;
		}
		else if(mimeType.startsWith("application/xml")
				|| mimeType.startsWith("text/xml")) {
			return XML;
		}
		
		RestFormatException e = new RestFormatException("no RestFormat applies to mimeType: " + mimeType);
		logger.debug(e.getMessage(), e);
		throw e;
	}
}
