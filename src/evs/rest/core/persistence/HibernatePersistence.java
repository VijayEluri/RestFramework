package evs.rest.core.persistence;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

/**
 * hibernate implementation of the persistence layer
 */
public class HibernatePersistence implements RestPersistence {

	/* logger */
	private static Logger logger = Logger.getLogger(HibernatePersistence.class);

	/* singleton pattern */
	private static HibernatePersistence instance;

	public static HibernatePersistence getInstance() {
		if (instance == null) {
			instance = new HibernatePersistence();
		}
		return instance;
	}

	private EntityManagerFactory emf;
	private EntityManager em;

	private ValidatorFactory factory;
	private Validator validator;

	private HibernatePersistence() {
		emf = Persistence.createEntityManagerFactory("rest");
		em = emf.createEntityManager();
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Override
	public <T> T create(T object) throws RestPersistenceValidationException,
			RestPersistenceException {
		// TODO object already exists
		logger.debug("create request for object" + object.toString());
		validate(object);
		try {
			em.getTransaction().begin();
			em.persist(object);
			em.getTransaction().commit();
			detach(object);
			logger.debug("saved: " + object.toString());
			return object;
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			throw new RestPersistenceException(e);
		}
	}

	@Override
	public <T> T read(Object id, Class<T> clazz)
			throws RestPersistenceException {
		logger.debug("read request with id: " + id.toString());
		try {
			T object = em.find(clazz, id);
			if (object == null) {
				logger.debug("could not find object with id: " + id.toString());
				return null;
			} else {
				logger.debug("object retrieved: " + object.toString());
				detach(object);
				return object;
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			throw new RestPersistenceException(e);
		}
	}

	@Override
	public <T> T update(T object) throws RestPersistenceValidationException,
			RestPersistenceException {
		logger.debug("update request for object: " + object.toString());
		validate(object);
		try {
			em.getTransaction().begin();
			T merged = em.merge(object);
			em.getTransaction().commit();
			detach(object);
			logger.debug("updated object: " + object.toString());
			return merged;
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			throw new RestPersistenceException(e);
		}
	}

	@Override
	public <T> boolean delete(Object id, Class<T> clazz) throws RestPersistenceException {
		logger.debug("delete request for object with id: " + id.toString());
		try {
			em.getTransaction().begin();
			T object = em.find(clazz, id);
			Boolean success;
			if (object == null) {
				logger.debug("could not find object with id: " + id.toString());
				// TODO: delete exceptions
				success = false;
			} else {
				logger.debug("object retrieved: " + object.toString());
				em.remove(object);
				success = true;
			}
			em.getTransaction().commit();
			return success;
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			throw new RestPersistenceException(e);
		}
	}

	@Override
	public <T> List<T> search(String text, Class<T> clazz, List<String> fields) throws RestPersistenceException {
		logger.debug("search request for text: " + text);
		text = "%" + text + "%";

		try {
			org.hibernate.Session session = (Session) em.getDelegate();
			Criteria crit = session.createCriteria(clazz);
	
			Junction junction = Restrictions.disjunction();
			for (String field : fields) {
				junction.add(Restrictions.like(field, text));
			}
			crit.add(junction);
	
			// TODO: search limit & pagination
			// crit.setMaxResults(10);
			List<T> results = crit.list();
			if (results != null) {
				logger.debug("found " + results.size() + "items");
				return results;
			} else {
				logger.debug("search had no results");
				return null;
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			throw new RestPersistenceException(e);
		}
	}

	private void detach(Object entity) {
		return;
		// em.detach(entity);
		// org.hibernate.Session session = (Session) em.getDelegate();
		// session.evict(entity);
		
		/*
		 * private <T> T getRealObject(T obj) {
  Hibernate.initialize(obj);
  if (obj instanceof HibernateProxy) {
   return (T) ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
  }
  return obj;
 }
		 */
	}

	private void validate(Object object)
			throws RestPersistenceValidationException {
		Set<ConstraintViolation<Object>> violations = validator
				.validate(object);
		if (violations != null && violations.size() > 0) {
			logger.debug("validation violations: " + violations.toString());
			throw new RestPersistenceValidationException(violations);
		}
	}

}
