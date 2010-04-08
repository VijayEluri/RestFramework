package evs.rest.core.marshal;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class XStreamJSONMarshaller extends XStreamXMLMarshaller {
	private static Logger logger = Logger.getLogger(XStreamJSONMarshaller.class);

	public XStreamJSONMarshaller() {
		xstream = new XStream(new JettisonMappedXmlDriver());
		/*xstream.registerConverter(new CollectionConverter(xstream.getMapper()) {
			public boolean canConvert(Class type) {
				return type == org.hibernate.collection.PersistentBag.class;
			}
		});  */  
		//xstream.aliasAttribute(null, "class");
		//xstream.setMode(XStream.NO_REFERENCES);
	}

}
