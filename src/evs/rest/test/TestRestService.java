package evs.rest.test;

import junit.framework.Assert;

import org.junit.Test;

import evs.rest.core.RestService;
import evs.rest.core.annotations.RestEntity;
import evs.rest.core.annotations.RestPath;
import evs.rest.core.persistence.HibernatePersistence;
import evs.rest.core.persistence.RestPersistence;
import evs.rest.demo.domain.Item;
import evs.rest.demo.domain.Placement;
import evs.rest.demo.domain.Rack;


public class TestRestService {
	
	private class Car {
		
	}
	 
	@RestEntity(Car.class)
	@RestPath("path")
	private class CustomPathService extends RestService {
		public CustomPathService() throws Exception {
			super();
		}
		private static final long serialVersionUID = 1L;
	}
	
	@RestEntity(Car.class)
	private class DefaultPathService extends RestService {
		public DefaultPathService() throws Exception {
			super();
		}
		private static final long serialVersionUID = 1L;
	}
	
	
	
	@Test
	public void testCustomPathValid() throws Exception {
		RestService service = new CustomPathService();
		Assert.assertEquals(service.getPath(), "path/*");
	}
	
	@Test
	public void restDefaultPath() throws Exception {
		RestService service = new DefaultPathService();
		Assert.assertEquals(service.getPath(), "cars/*");
	}

}
