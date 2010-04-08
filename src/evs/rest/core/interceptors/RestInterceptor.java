package evs.rest.core.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RestInterceptor {
	
	public void beforeServicePost(HttpServletRequest req, HttpServletResponse resp);
	public void afterServicePost(HttpServletRequest req, HttpServletResponse resp);
	public void beforeDBCreate(Object object);
	public void afterDBCreate(Object object);

	public void beforeServiceGet(HttpServletRequest req, HttpServletResponse resp);
	public void afterServiceGet(HttpServletRequest req, HttpServletResponse resp);
	public void beforeDBRead(Object id);
	public void afterDBRead(Object object);

	public void beforeServicePut(HttpServletRequest req, HttpServletResponse resp);
	public void afterServicePut(HttpServletRequest req, HttpServletResponse resp);
	public void beforeDBUpdate(Object object);
	public void afterDBUpdate(Object object);

	public void beforeServiceDelete(HttpServletRequest req, HttpServletResponse resp);
	public void afterServiceDelete(HttpServletRequest req, HttpServletResponse resp);
	public void beforeDBDelete(Object object);
	public void afterDBDelete(Object object);
	
	public void beforeServiceSearch(HttpServletRequest req, HttpServletResponse resp);
	public void afterServiceSearch(HttpServletRequest req, HttpServletResponse resp);
	public void beforeDBSearch(String text, Class<Object> entityClass, List<String> fields);
	public void afterDBSearch(List<Object> result);

}
