package evs.rest.test;

import evs.rest.core.persistence.HibernatePersistence;

public class TestRestDataHibernate extends TestRestData {
	
	public TestRestDataHibernate() {
		this.persistence = HibernatePersistence.getInstance();
	}

}
