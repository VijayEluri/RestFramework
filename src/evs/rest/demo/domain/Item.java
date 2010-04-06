package evs.rest.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
public class Item {
	
	/*** member variables ***/
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	protected String name;
	
	protected String description;
	
	@Min(1)
	protected Integer size;
	
	/*** constructor ***/
	
	public Item() { }
	
	public Item(String name, String description, Integer size) {
		this.name = name;
		this.description = description;
		this.size = size;
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
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	
	@Override
	public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof Item) ) return false;

        final Item item = (Item) other;

        if(!RackUtil.compare(this.getName(), item.getName())) return false;
        if(!RackUtil.compare(this.getDescription(), item.getDescription())) return false;
        if(!RackUtil.compare(this.getSize(), item.getSize())) return false;

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
			if(this.getSize() != null) hc += this.getSize().hashCode();
			return hc;
		}
	}
	
	
}
