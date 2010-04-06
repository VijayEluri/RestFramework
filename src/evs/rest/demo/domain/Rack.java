package evs.rest.demo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

import evs.rest.core.util.RestUtil;



@Entity
public class Rack {

	private static Logger logger = Logger.getLogger(Rack.class);
	
	/*** member variables ***/
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	protected String name;
	
	protected String description;
	
	protected Integer place;
	
	@OneToMany
	protected List<Placement> placements = new ArrayList<Placement>();
	
	/*** constructor ***/
	
	public Rack() { }
	
	public Rack(String name, String description, Integer place) {
		this.name = name;
		this.description = description;
		this.place = place;
	}
	
	/*** getters and setters ***/
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getPlace() {
		return place;
	}
	public void setPlace(Integer place) {
		this.place = place;
	}
	public List<Placement> getPlacements() {
		return placements;
	}
	public void setPlacements(List<Placement> placements) {
		this.placements = placements;
	}
	
	public static int calculatePlacedSize(Rack rack) {
		Integer placedSize = 0;
		
		logger.debug("calculating placed size");
		if(rack.getPlacements() != null) {
			for(Placement pl : rack.getPlacements()) {
				placedSize += pl.getItem().getSize() * pl.getAmount();
				logger.debug(pl.getItem().getSize() + " (size) * " + pl.getAmount() + " (amount) of item " + pl.getItem().getName());
			}
		}
		logger.debug("total size is " + placedSize);
		return placedSize;
	}
	
	@Override
	public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof Rack) ) return false;

        final Rack rack = (Rack) other;

        if(this.getName() == null) {
        	if(!(rack.getName() == null)) return false;
        }
        if(!RackUtil.compare(this.getName(), rack.getName())) return false;
        if(!RackUtil.compare(this.getDescription(), rack.getDescription())) return false;
        if(!RackUtil.compare(this.getPlace(), rack.getPlace())) return false;
        
    	if(this.getPlacements() == null) return false;
    	if(rack.getPlacements() == null) return false;
    	if(this.getPlacements().size() != rack.getPlacements().size()) return false;
    	
    	for(Placement placement : this.getPlacements()) {
    		boolean found = false;
    		for(Placement otherPlacement : rack.getPlacements()) {
    			if(placement.hashCode() == otherPlacement.hashCode()) found = true;
    			//if(placement.equals(otherPlacement)) found = true;
    		}
    		if(!found) return false;
    	}
       
        return true;
	}
	
	@Override
	public int hashCode() {
		if(this.getId() != null) {
			return this.getId().hashCode();
		}
		else {
			int hc = 0;
			if(this.getName() != null) hc += this.getName().hashCode();
			if(this.getDescription() != null) hc += this.getDescription().hashCode();
			if(this.getPlace() != null) hc += this.getPlace().hashCode();
			return hc;
		}
	}
	
}
