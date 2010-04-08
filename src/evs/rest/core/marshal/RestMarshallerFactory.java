package evs.rest.core.marshal;

import org.apache.log4j.Logger;


public abstract class RestMarshallerFactory {
	public static Logger logger = Logger.getLogger(RestMarshallerFactory.class);
	
	private static RestMarshaller jsonMarshaller = new XStreamJSONMarshaller();
	private static RestMarshaller xmlMarshaller = new XStreamXMLMarshaller();
	
	/**
	 * provides a marshaller instance for a given @RestFormat format
	 * @param formats the RestFormat
	 * @return the marshaller on success, else null 
	 */
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
}
