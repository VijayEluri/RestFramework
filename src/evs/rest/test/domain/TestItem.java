package evs.rest.test.domain;

import junit.framework.Assert;

import org.junit.Test;

import evs.rest.demo.domain.Item;

public class TestItem {
	
	

	@Test
	public void testItemEquals() {
		Item item = new Item("Item", "My Item", 2);
		Item sameItem = new Item("Item", "My Item", 2);
		
		Assert.assertEquals(item, item);
		Assert.assertEquals(item, sameItem);
		
		Assert.assertFalse(item.equals(new Item("ItemA", "My Item", 2)));
		Assert.assertFalse(item.equals(new Item("Item", "My ItemA", 2)));
		Assert.assertFalse(item.equals(new Item("Item", "My Item", 3)));
	}
	
	@Test
	public void testItemEquals2() {
		Item item = new Item();
		Item sameItem = new Item();
		
		Assert.assertEquals(item, item);
		Assert.assertEquals(item, sameItem);
	}
	


}
