package evs.rest.demo.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import evs.rest.demo.validation.PlacementConstraint;

@Entity
@PlacementConstraint
public class Placement {

	@EmbeddedId
	protected PlacementPk id = new PlacementPk();
	
	/*** member variables (references in composite primary key class) ***/

	protected Integer amount;
	protected String storing_position;
	
	/*** constructor ***/
	
	public Placement() { }
	
	public Placement(Rack rack, Item item, Integer amount, String storing_position) {
		this.id.rack = rack;
		this.id.item = item;
		this.amount = amount;
		this.storing_position = storing_position;
	}
	
	/*** getters and setters ***/

	public Rack getRack() {
		return this.id.rack;
	}
	public void setRack(Rack rack) {
		this.id.rack = rack;
	}
	public Item getItem() {
		return this.id.item;
	}
	public void setItem(Item item) {
		this.id.item = item;
	}

	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getStoring_position() {
		return this.storing_position;
	}
	public void setStoring_position(String storingPosition) {
		this.storing_position = storingPosition;
	}

	/**
	 * composite private key class
	 */
	@Embeddable
	public class PlacementPk implements Serializable {

		@ManyToOne
		protected Rack rack;

		@ManyToOne
		protected Item item;

		public PlacementPk() {} 

		// serializeable implementation follows

		private static final long serialVersionUID = 1L;

		public boolean equals(Object obj) {
			//TODO: equals
			return false;
		}

		public int hashCode() {      		
			//TODO: hashcode
			return 0;
		}
	}

}
