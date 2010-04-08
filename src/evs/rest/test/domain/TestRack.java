package evs.rest.test.domain;

import junit.framework.Assert;

import org.junit.Test;

import evs.rest.demo.domain.Item;
import evs.rest.demo.domain.Placement;
import evs.rest.demo.domain.Rack;

public class TestRack {
	
	

	@Test
	public void testRackEquals() {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		Rack sameRack = new Rack("Rack", "A 5 slot rack", 5);
		
		Assert.assertEquals(rack, rack);
		Assert.assertEquals(rack, sameRack);
		
		Assert.assertFalse(rack.equals(new Rack("RackA", "A 5 slot rack", 5)));
		Assert.assertFalse(rack.equals(new Rack("Rack", "A 4 slot rack", 5)));
		Assert.assertFalse(rack.equals(new Rack("Rack", "A 5 slot rack", 4)));
	}
	
	@Test
	public void testRackEqualsDeep() {
		Rack rack = new Rack();
		Rack sameRack = new Rack();

		Item item = new Item("Item", "My Item", 2);
		Item sameItem = new Item("Item", "My Item", 2);
		Item otherItem = new Item("Item", "My Item", 2);
		
		rack.getPlacements().add(new Placement(rack, item, 1, "somewhere"));
		sameRack.getPlacements().add(new Placement(sameRack, item, 1, "somewhere"));
		Assert.assertEquals(rack, sameRack);
		
		sameRack.getPlacements().clear();
		sameRack.getPlacements().add(new Placement(sameRack, sameItem, 1, "somewhere"));
		Assert.assertEquals(rack, sameRack);
		
		sameRack.getPlacements().add(new Placement(sameRack, otherItem, 1, "somewhere"));
		Assert.assertFalse(rack.equals(sameRack));
		
		//more fun
		rack.getPlacements().add(new Placement(rack, otherItem, 1, "somewhere"));
		
		rack.getPlacements().add(new Placement(rack, otherItem, 1, "somewhere"));
		sameRack.getPlacements().add(new Placement(sameRack, otherItem, 1, "somewhere"));
		Assert.assertEquals(rack, sameRack);
		
		rack.getPlacements().add(new Placement(rack, otherItem, 2, "somewhereA"));
		sameRack.getPlacements().add(new Placement(sameRack, otherItem, 2, "somewhereA"));
		Assert.assertEquals(rack, sameRack);
		
		sameRack.getPlacements().remove(1);		
		Assert.assertFalse(rack.equals(sameRack));
		
		
		
	}

	@Test
	public void testRackEquals2() {
		Rack rack = new Rack();
		Rack sameRack = new Rack();
		
		Assert.assertEquals(rack, rack);
		Assert.assertEquals(rack, sameRack);
		
	}


}
