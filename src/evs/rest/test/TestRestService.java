package evs.rest.test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import evs.rest.core.RestServer;
import evs.rest.core.marshal.JSONMarshaller;
import evs.rest.core.marshal.RestMarshaller;
import evs.rest.core.marshal.RestMarshallerException;
import evs.rest.core.util.RestConst;
import evs.rest.demo.RackService;
import evs.rest.demo.domain.Rack;


public class TestRestService {
	private static final String RACK_SERVICE = "http://localhost:" + RestConst.DEFAULT_SERVER_PORT + "/racks";
	private static Logger logger = Logger.getLogger(TestRestService.class);
	
	@BeforeClass
	public static void prepareClass() throws Exception {
		RestServer provider = new RestServer();
		RackService service = new RackService();
		provider.addService(service);
		provider.setServerPath("/");

		provider.start();
	}
	
	RestMarshaller marshaller = new JSONMarshaller();
	HttpClient httpclient = new DefaultHttpClient();
	
	@Before
	public void prepare() {
		
	}
	
	@Test
	public void testPost() throws Exception {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		Rack saved = postRack(rack);
		assertEqualRacks(rack, saved);
		
		//TODO: create & tests items & placements 

		/*
		Integer itemSlots = 1;
		Integer placementAmount = 1;
		logger.debug("new item with " + itemSlots + " slots, placed " + placementAmount + " times");
		
		String name = "Item*" + itemSlots;
		Item item = new Item(name, "This item needs " + itemSlots + " slot(s)", itemSlots);
		persistence.create(item);

		Placement placement = new Placement(rack, item, placementAmount, "");
		rack.getPlacements().add(placement);
		persistence.create(placement);*/
		
		/*
		
		createAndPlaceItem(rack, 1, 1);	//itemSlots, placementAmount
		createAndPlaceItem(rack, 2, 2);
		
		logger.debug("the 5 slot rack has to be full now");
		Assert.assertEquals(5, Rack.calculatePlacedSize(rack));
		// the rack is full now
		
		try {
			logger.debug("placing over-limit item");
			createAndPlaceItem(rack, 1, 1);	//itemSlots, placementAmount
			Assert.fail("should not reach");
		} catch (RestPersistenceValidationException e) {
			logger.debug("over-limit validation exception");
			logger.debug(e);
			return;
		}*/
	}

	@Test
	public void testGet() throws Exception {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		Rack saved = postRack(rack);
		
		Rack retrieved = getRack(saved.getId());
		
		assertEqualRacks(rack, retrieved);
	}

	
	@Test 
	public void testPut() throws Exception {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		Rack saved = postRack(rack);
		
		saved.setName("Updated Rack");
		Rack updated = putRack(saved);
		
		assertEqualRacks(saved, updated);
		
		Rack retrieved = getRack(updated.getId());
		assertEqualRacks(saved, retrieved);
	}
	
	@Test
	public void testDelete() throws Exception {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		Rack saved = postRack(rack);
		
		int responseCode = deleteRack(saved.getId());
		Assert.assertEquals(HttpStatus.OK_200, responseCode);
	}
	
	@Test
	public void testSearch() throws Exception {
		Hashtable<Long, Rack> rackReference = new Hashtable<Long, Rack>();
		Rack rack;
		
		//included
		rack = new Rack("RackAB", "A 5 slot rack", 5);
		rack = postRack(rack);
		rackReference.put(rack.getId(), rack);
		rack = new Rack("RackABC", "A 5 slot rack", 5);
		rack = postRack(rack);
		rackReference.put(rack.getId(), rack);
		
		//excluded
		postRack(new Rack("RackXYZ", "A 5 slot rack", 5));
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("name");
		
		HttpGet httpget = new HttpGet(RACK_SERVICE + "/search?text=AB");
		HttpResponse response = httpclient.execute(httpget);
		List<Rack> result = marshaller.read(ArrayList.class, response.getEntity().getContent());
		
		Assert.assertNotNull("results expected", result);
		Assert.assertEquals("results ammount", rackReference.size(), result.size());

		for(Rack foundRack : result) {
			Rack refRack = rackReference.get(foundRack.getId());
			assertEqualRacks(refRack, foundRack);
		}
		
	}

	private Rack postRack(Rack rack) throws IOException,
			ClientProtocolException, RestMarshallerException {
		HttpPost httppost = new HttpPost(RACK_SERVICE);
		HttpEntity entity = new EntityTemplate(new MarshallerCP(rack, marshaller));
		httppost.setEntity(entity);
		
		HttpResponse response = httpclient.execute(httppost);
		logger.debug("status: " + response.getStatusLine().getStatusCode());
		Rack saved = marshaller.read(Rack.class, response.getEntity().getContent());
		return saved;
	}

	private Rack getRack(Long id) throws IOException,
			ClientProtocolException, RestMarshallerException {
		HttpGet httpget = new HttpGet(RACK_SERVICE + "/" + id);
		HttpResponse response = httpclient.execute(httpget);
		Rack retrieved = marshaller.read(Rack.class, response.getEntity().getContent());
		return retrieved;
	}

	private Rack putRack(Rack rack) throws IOException,
			ClientProtocolException, RestMarshallerException {
		HttpPut httpput = new HttpPut(RACK_SERVICE);
		HttpEntity entity = new EntityTemplate(new MarshallerCP(rack, marshaller));
		httpput.setEntity(entity);
		
		HttpResponse response = httpclient.execute(httpput);
		logger.debug("status: " + response.getStatusLine().getStatusCode());
		Rack updated = marshaller.read(Rack.class, response.getEntity().getContent());
		return updated;
	}
	

	private int deleteRack(Long id) throws ClientProtocolException, IOException {
		HttpDelete httpdelete = new HttpDelete(RACK_SERVICE + "/" + id);
		HttpResponse response = httpclient.execute(httpdelete);
		
		logger.debug("status: " + response.getStatusLine().getStatusCode());
		return response.getStatusLine().getStatusCode();
	}
	

	private void assertEqualRacks(Rack rack, Rack saved) {
		Assert.assertEquals(rack.getName(), saved.getName());
		Assert.assertEquals(rack.getDescription(), saved.getDescription());
		Assert.assertEquals(rack.getPlace(), saved.getPlace());
		//TODO: in-depth rack comparison, use/implement rack.equals
	}
	
	

	private class MarshallerCP implements ContentProducer {
		
		private Object object;
		private RestMarshaller marshaller;
		
		public MarshallerCP(Object object, RestMarshaller marshaller)  {
			this.object = object;
			this.marshaller = marshaller;
		}
		
		@Override
		public void writeTo(OutputStream stream) throws IOException {
			try {
				marshaller.write(object, stream);
			} catch (RestMarshallerException e) {
				throw new IOException(e);
			}
		}
		
	}

}
