package evs.rest.test.domain;

import junit.framework.Assert;

import org.junit.Test;

import evs.rest.demo.domain.Item;
import evs.rest.demo.domain.Placement;
import evs.rest.demo.domain.Rack;

public class TestPlacement {
	
	

	@Test
	public void testPlacementEquals() {
		Item item = new Item();
		Rack rack = new Rack();

		Placement placement = new Placement(rack, item, 1, "somewhere");
		Placement samePlacement = new Placement(rack, item, 1, "somewhere");
		
		Assert.assertEquals(placement, placement);
		Assert.assertEquals(placement, samePlacement);
		
		Assert.assertNotSame(item, new Placement(null, item, 1, "somewhere"));
		Assert.assertNotSame(item, new Placement(rack, null, 1, "somewhere"));
		Assert.assertNotSame(item, new Placement(rack, item, 2, "somewhere"));
		Assert.assertNotSame(item, new Placement(rack, item, 1, "somewhereA"));
	}
	
	@Test
	public void testPlacementEquals2() {

		Placement placement = new Placement(null, null, 1, "somewhere");
		Placement samePlacement = new Placement(null, null, 1, "somewhere");
		
		Assert.assertEquals(placement, placement);
		Assert.assertEquals(placement, samePlacement);
		
		Assert.assertFalse(placement.equals(new Placement(null, null, 2, "somewhere")));
		Assert.assertFalse(placement.equals(new Placement(null, null, 1, "somewhereA")));
		
	}
	

	@Test
	public void testPlacementEquals3() {
		Placement placement = new Placement();
		Placement samePlacement = new Placement();
		
		Assert.assertEquals(placement, placement);
		Assert.assertEquals(placement, samePlacement);
	}
	


}
