package evs.rest.demo;

import evs.rest.core.RestService;
import evs.rest.core.annotations.RestAcceptedFormats;
import evs.rest.core.annotations.RestEntity;
import evs.rest.core.annotations.RestFormat;
import evs.rest.core.annotations.RestId;
import evs.rest.core.annotations.RestPath;
import evs.rest.demo.domain.Rack;

@RestEntity(Rack.class)
@RestId(Long.class)
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
