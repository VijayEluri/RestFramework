package evs.rest.core.marshal;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamXMLMarshaller implements RestMarshaller {
	private static Logger logger = Logger.getLogger(XStreamXMLMarshaller.class);

	protected XStream xstream;

	public XStreamXMLMarshaller() {
		xstream = new XStream(new DomDriver());
	}

	@Override
	public <T> T read(Class<T> clazz, InputStream input) throws RestMarshallerException {
		logger.debug("reading of class: " + clazz.toString());
		try {
			T object = (T) xstream.fromXML(input);
			if (object.getClass() != clazz) {
				RestMarshallerException e = new RestMarshallerException("deserialized object class: " + object.getClass()
						+ " doesn't match expected: " + clazz);
				logger.debug(e.getMessage(), e);
				throw e;
			}
			logger.debug("finished reading: " + object.toString());
			return object;
		} catch (Exception e) {
			logger.debug("exception while parsing", e);
			throw new RestMarshallerException("parse exception");
		}
	}

	@Override
	public <T> void write(T object, OutputStream output) throws RestMarshallerException {
		logger.debug("writing object: " + object.toString());
		try {
			xstream.toXML(object, output);
			logger.debug("finished writing");
		} catch (Exception e) {
			logger.debug("exception while parsing", e);
			throw new RestMarshallerException("parse exception");
		}
	}
}
