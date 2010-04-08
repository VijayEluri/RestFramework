package evs.rest.test.misc;

import evs.rest.core.persistence.HibernatePersistence;

public class TestRestDataHibernate extends TestRestData {
	
	public TestRestDataHibernate() {
		this.persistence = HibernatePersistence.getInstance();
	}

}
