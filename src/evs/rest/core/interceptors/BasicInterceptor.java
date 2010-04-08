package evs.rest.core.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class BasicInterceptor implements RestInterceptor {
	private static Logger logger = Logger.getLogger(BasicInterceptor.class);

	@Override
	public void afterDBCreate(Object object) {
		logger.debug("afterDBCreate");
	}

	@Override
	public void afterServicePost(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("afterServicePost");
	}

	@Override
	public void beforeDBCreate(Object object) {
		logger.debug("beforeDBCreate");
	}

	@Override
	public void beforeServicePost(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("beforeServicePost");
	}

	@Override
	public void afterDBDelete(Object object) {
		logger.debug("afterDBDelete");
	}

	@Override
	public void afterDBRead(Object object) {
		logger.debug("afterDBRead");
	}

	@Override
	public void afterDBSearch(List<Object> result) {
		logger.debug("afterDBSearch");
	}

	@Override
	public void afterDBUpdate(Object object) {
		logger.debug("afterDBUpdate");
	}

	@Override
	public void afterServiceDelete(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("afterServiceDelete");
	}

	@Override
	public void afterServiceGet(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("afterServiceGet");
	}

	@Override
	public void afterServicePut(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("afterServicePut");
	}

	@Override
	public void afterServiceSearch(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("afterServiceSearch");
	}

	@Override
	public void beforeDBDelete(Object object) {
		logger.debug("beforeDBDelete");
	}

	@Override
	public void beforeDBRead(Object id) {
		logger.debug("beforeDBRead");
	}

	@Override
	public void beforeDBSearch(String text, Class<Object> entityClass, List<String> fields) {
		logger.debug("beforeDBSearch");
	}

	@Override
	public void beforeDBUpdate(Object object) {
		logger.debug("beforeDBUpdate");
	}

	@Override
	public void beforeServiceDelete(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("beforeServiceDelete");
	}

	@Override
	public void beforeServiceGet(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("beforeServiceGet");
	}

	@Override
	public void beforeServicePut(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("beforeServicePut");
	}

	@Override
	public void beforeServiceSearch(HttpServletRequest req, HttpServletResponse resp) {
		logger.debug("beforeServiceSearch");
	}

}
