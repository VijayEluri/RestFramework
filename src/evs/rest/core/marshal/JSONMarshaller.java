package evs.rest.core.marshal;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import evs.rest.core.RestService;

public class JSONMarshaller implements RestMarshaller {
	private static Logger logger = Logger.getLogger(JSONMarshaller.class);

	protected XStream xstream;

	public JSONMarshaller() {
		xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.setMode(XStream.NO_REFERENCES);
	}

	@Override
	public Object read(Class<Object> clazz, InputStream input)
	throws RestMarshallerException {
		try {
			Object object = xstream.fromXML(input);
			if(object.getClass() != clazz) {
				RestMarshallerException e = new RestMarshallerException("deserialized object class: " + object.getClass() + " doesn't match expected: " + clazz);
				logger.debug(e.getMessage(), e);
				throw e;
			}
			return object;
		}
		catch(Exception e) {
			logger.debug("exception while parsing", e);
			throw new RestMarshallerException("parse exception");
		}
	}	
	
	@Override
	public void write(Object object, OutputStream output)
	throws RestMarshallerException {
	
		xstream.toXML(object, output);
	}



}
