package evs.rest.core.marshal;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * marshalling interface, abstracting different marshallers which convert from text streams to objects
 */
public interface RestMarshaller {
	
	/**
	 * reads an object
	 * @param clazz object's class
	 * @param input the input stream to deserialize the object from
	 * @return the object
	 */
	public Object read(Class<Object> clazz, InputStream input) throws RestMarshallerException;
	
	public void write(Object object, OutputStream output) throws RestMarshallerException;

}
