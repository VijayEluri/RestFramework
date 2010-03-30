package evs.rest.core.persistance;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import evs.rest.core.RestServiceConfig;

/**
 * hibernate implementation of the persistance layer
 */
public class HibernatePersistance implements RestPersistance {

	/* logger */
	private static Logger logger = Logger.getLogger(HibernatePersistance.class);
	
	private RestServiceConfig config;
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public HibernatePersistance() {
		emf = Persistence.createEntityManagerFactory("rest");
		em = emf.createEntityManager();
	}

	@Override
	public Object create(Object object) {
		// TODO Auto-generated method stub
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
		return object;
	}
	
	@Override
	public Object read(Object id) {
		logger.debug("read request with id: " + id.toString());
		Object object = em.find(config.getEntityClass(), id);
		if(object == null) {
			logger.debug("could not find object with id" + id.toString());
			return null;
		}
		else {
			logger.debug("object retrieved" + object.toString());
			return object;
		}
	}


	@Override
	public Object update(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public boolean delete(Object id) {
		// TODO Auto-generated method stub
		return false;
	}


}
