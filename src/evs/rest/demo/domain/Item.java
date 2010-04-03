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
	
	
}
