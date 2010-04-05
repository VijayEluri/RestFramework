package evs.rest.core.marshal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class RestMarshallerFactory {
	
	private static RestMarshaller jsonMarshaller = new JSONMarshaller();
	

	public static RestMarshaller getMarshaller(RestFormat format) {
		switch (format) {
		case JSON:
			return jsonMarshaller;
		}
		//TODO: xml
		return null;
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
				//no format specified, returning first of supported formats
				return RestMarshallerFactory.getMarshaller(formats.get(0));
			}
		} catch (RestFormatException e) {
			//TODO: requested format unknown
			return null;
		}
	}
}
