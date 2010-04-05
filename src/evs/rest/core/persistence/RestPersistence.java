package evs.rest.core.persistence;

import java.util.List;

/**
 * persistence layer interface for RESTful @RestService
 * 
 * provides storage CRUD functionality
 */
public interface RestPersistence {
	

	/**
	 * create a new object within the database
	 * @param object the object to save
	 * @return the created object
	 * @throws RestPersistenceValidationException 
	 */
	<T> T create(T object) throws RestPersistenceValidationException;
	
	/**
	 * retrieves an existing object from the database
	 * @param id the object identifier	 
	 * @param clazz the entityClass to retrieve
	 * @return the object, if successfull. null if not
	 */
	<T> T read(Object id, Class<T> clazz);
	
	/**
	 * updates an existing object within the database
	 * @param object the object, containing identifier and updates values	 
	 * @return the updated object
	 * @throws RestPersistenceValidationException 
	 */
	<T> T update(T object) throws RestPersistenceValidationException;
	
	/**
	 * deletes an existing object from the database
	 * @param id the object identifier	 
	 * @param clazz type of object to delete
	 * @return true for success 
	 */
	<T> boolean delete(Object id, Class<T> clazz);
	
	/**
	 * full-text-search
	 * @param text text pattern
	 * @param clazz type of objects to search for
	 * @param fields fields to include within search
	 * @return a list of matching objects or null
	 */
	<T> List<T> search(String text, Class<T> clazz, List<String> fields);

}
