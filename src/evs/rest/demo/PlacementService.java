package evs.rest.demo;

import evs.rest.core.RestService;
import evs.rest.core.annotations.RestEntity;
import evs.rest.demo.domain.Placement;

@RestEntity(Placement.class)
public class PlacementService extends RestService {

	public PlacementService() throws Exception {
		super();
	} 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
