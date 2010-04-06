package evs.rest.core.marshal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import evs.rest.test.TestRestService;


public class RestMarshallerFactory {
	private static Logger logger = Logger.getLogger(RestMarshallerFactory.class);
	
	private static RestMarshaller jsonMarshaller = new XStreamJSONMarshaller();
	private static RestMarshaller xmlMarshaller = new XStreamXMLMarshaller();
	
	public static RestMarshaller getMarshaller(RestFormat format) {
		RestMarshaller marshaller = null;
		switch (format) {
		case JSON:
			marshaller = jsonMarshaller;
			break;
		case XML:
			marshaller = xmlMarshaller;
			break;
		}
		logger.debug("marshaller is: " + marshaller.toString());
		return marshaller;
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
		} catch (RestFormatException e) {
			//TODO: requested format unknown
			return null;
		}
	}
}
