package evs.rest.core.persistance;

/**
 * persistance layer interface for RESTful @RestService
 * 
 * provides storage CRUD functionality
 */
public interface RestPersistance {
	
	/**
	 * create a new object within the database
	 * @param object the object to save
	 * @return the created object
	 */
	Object create(Object object);
	
	/**
	 * retrieves an existing object from the database
	 * @param id the object identifier
	 * @return the object, if successfull. null if not
	 */
	Object read(Object id);
	
	/**
	 * updates an existing object within the database
	 * @param object the object, containing identifier and updates values
	 * @return the updated object
	 */
	Object update(Object object);
	
	/**
	 * deletes an existing object from the database
	 * @param id the object identifier
	 * @return true for success 
	 */
	boolean delete(Object id);

}
