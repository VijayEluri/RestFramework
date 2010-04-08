package evs.rest.demo;

import evs.rest.core.RestService;
import evs.rest.core.annotations.RestEntity;
import evs.rest.core.annotations.RestId;
import evs.rest.demo.domain.Item;

@RestEntity(Item.class)
@RestId(Long.class)
public class ItemService extends RestService {

	public ItemService() throws Exception {
		super();
	} 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
