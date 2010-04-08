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
	 * @throws RestMarshallerException
	 */
	public <T> T read(Class<T> clazz, InputStream input) throws RestMarshallerException;
	
	/**
	 * writes an object
	 * @param object
	 * @param output the output stream to serialize the object to
	 * @throws RestMarshallerException
	 */
	public <T> void write(T object, OutputStream output) throws RestMarshallerException;

}
