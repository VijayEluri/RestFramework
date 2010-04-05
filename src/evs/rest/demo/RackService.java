package evs.rest.demo;

import evs.rest.core.RestService;
import evs.rest.core.annotations.RestAcceptedFormats;
import evs.rest.core.annotations.RestEntity;
import evs.rest.core.annotations.RestId;
import evs.rest.core.annotations.RestSearchIndexedFields;
import evs.rest.core.annotations.RestSearchPath;
import evs.rest.core.marshal.RestFormat;
import evs.rest.demo.domain.Rack;

@RestEntity(Rack.class)
@RestId(Long.class)
//@RestPath("rack")
@RestAcceptedFormats({RestFormat.JSON, RestFormat.XML})
@RestSearchIndexedFields({"name", "description"})
@RestSearchPath("search")
public class RackService extends RestService {

	public RackService() throws Exception {
		super();
	} 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
