package evs.rest.demo.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
		this.id.setRack(rack);
		this.id.setItem(item);
		this.amount = amount;
		this.storing_position = storing_position;
	}
	
	/*** getters and setters ***/

	public Rack getRack() {
		return this.id.getRack();
	}
	public void setRack(Rack rack) {
		this.id.setRack(rack);
	}
	public Item getItem() {
		return this.id.getItem();
	}
	public void setItem(Item item) {
		this.id.setItem(item);
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
	
	@Override
	public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof Placement) ) return false;

        final Placement placement = (Placement) other;
        
        if(!RackUtil.compare(this.getItem(), placement.getItem())) return false;
        if(!RackUtil.compare(this.getRack(), placement.getRack())) return false;
        if(!RackUtil.compare(this.getAmount(), placement.getAmount())) return false;
        if(!RackUtil.compare(this.getStoring_position(), placement.getStoring_position())) return false;

        return true;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
	
	/**
	 * composite private key class
	 */
	@Embeddable
	public class PlacementPk implements Serializable {
		
		@OneToOne
		protected Item item;
		
		@ManyToOne
		protected Rack rack;

		public PlacementPk() {} 

		private static final long serialVersionUID = 1L;
		
		public Item getItem() {
			return item;
		}

		public void setItem(Item item) {
			this.item = item;
		}

		public Rack getRack() {
			return rack;
		}

		public void setRack(Rack rack) {
			this.rack = rack;
		}


		public boolean equals(Object other) {
	        if (this == other) return true;
	        if ( !(other instanceof PlacementPk) ) return false;
	        
	        final PlacementPk pk = (PlacementPk) other;
	        
	        if(!this.item.equals(pk.getItem())) return false;
	        if(!this.rack.equals(pk.getRack())) return false;
	        
			return true;
		}

		public int hashCode() {   
			int hc = 0;
			if(this.item != null) hc += this.item.hashCode();
			if(this.rack != null) hc += this.rack.hashCode();
			return hc;
		}
	}
	
	

}
