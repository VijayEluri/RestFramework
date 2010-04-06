package evs.rest.test;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import evs.rest.core.persistence.RestPersistence;
import evs.rest.core.persistence.RestPersistenceException;
import evs.rest.core.persistence.RestPersistenceValidationException;
import evs.rest.demo.domain.Item;
import evs.rest.demo.domain.Placement;
import evs.rest.demo.domain.Rack;

public abstract class TestRestData {
	
	private static Logger logger = Logger.getLogger(TestRestData.class);

	protected RestPersistence persistence;

	@Test
	public void testRackDataCreation() throws RestPersistenceValidationException, RestPersistenceException {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		persistence.create(rack);

		createAndPlaceItem(rack, 1, 1);	//itemSlots, placementAmount
		createAndPlaceItem(rack, 2, 2);
		
		logger.debug("the 5 slot rack has to be full now");
		Assert.assertEquals(5, Rack.calculatePlacedSize(rack));
		// the rack is full now
		
		try {
			logger.debug("placing over-limit item");
			createAndPlaceItem(rack, 1, 1);	//itemSlots, placementAmount
			Assert.fail("should not reach");
		} catch (RestPersistenceValidationException e) {
			logger.debug("over-limit validation exception");
			logger.debug(e);
			return;
		}

	}
	
	private void createAndPlaceItem(Rack rack, Integer itemSlots,
			Integer placementAmount) throws RestPersistenceValidationException, RestPersistenceException {
		
		logger.debug("new item with " + itemSlots + " slots, placed " + placementAmount + " times");
		
		String name = "Item*" + itemSlots;
		Item item = new Item(name, "This item needs " + itemSlots + " slot(s)", itemSlots);
		persistence.create(item);

		Placement placement = new Placement(rack, item, placementAmount, "");
		rack.getPlacements().add(placement);
		persistence.create(placement);
	}
}
