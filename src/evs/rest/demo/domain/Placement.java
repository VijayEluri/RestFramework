package evs.rest.demo.domain;

import evs.rest.demo.validation.PlacementConstraint;

@PlacementConstraint
public class Placement {
	
	protected Rack rack;
	protected Item item;
	
	protected Integer amount;
	protected String storing_position;
	
	public Rack getRack() {
		return rack;
	}
	public void setRack(Rack rack) {
		this.rack = rack;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getStoring_position() {
		return storing_position;
	}
	public void setStoring_position(String storingPosition) {
		storing_position = storingPosition;
	}
	
	
}
