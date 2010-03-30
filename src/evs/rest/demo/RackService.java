package evs.rest.demo;

import evs.rest.core.RestService;
import evs.rest.core.annotations.RestAcceptedFormats;
import evs.rest.core.annotations.RestEntity;
import evs.rest.core.annotations.RestFormat;
import evs.rest.core.annotations.RestPath;
import evs.rest.demo.domain.Rack;

@RestEntity(Rack.class)
//@RestPath("rack")
@RestAcceptedFormats({RestFormat.JSON, RestFormat.XML})
public class RackService extends RestService {

	public RackService() throws Exception {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
