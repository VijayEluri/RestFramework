package evs.rest.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.hibernate.sql.Update;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import evs.rest.core.RestServer;
import evs.rest.core.RestService;
import evs.rest.core.marshal.RestMarshaller;
import evs.rest.core.marshal.RestMarshallerException;
import evs.rest.core.marshal.XStreamJSONMarshaller;
import evs.rest.core.marshal.XStreamXMLMarshaller;
import evs.rest.demo.ItemService;
import evs.rest.demo.PlacementService;
import evs.rest.demo.RackService;
import evs.rest.demo.domain.Item;
import evs.rest.demo.domain.Placement;
import evs.rest.demo.domain.Rack;


public abstract class TestRestService {
	private static final String SERVICE_URL = "http://localhost:8777/";
	private static Logger logger = Logger.getLogger(TestRestService.class);
	
	private static RackService rackService;
	private static ItemService itemService;
	private static PlacementService placementService;
	
	protected static RestMarshaller marshaller;
	protected static String mimeType;
	
	@BeforeClass
	public static void prepareClass() throws Exception {
		RestServer provider = new RestServer();
		
		rackService = new RackService();
		provider.addService(rackService);

		itemService = new ItemService();
		provider.addService(itemService);
		
		placementService = new PlacementService();
		provider.addService(placementService);
		
		provider.setServerPath("/");

		provider.start();
	}
	
	
	//RestMarshaller marshaller = new XStreamXMLMarshaller(); 
	//String mimeType = "application/xml";
	
	HttpClient httpclient = new DefaultHttpClient();
	
	@Before
	public void prepare() {
		
	}
	
	@Test
	public void testPost() throws Exception {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		Rack savedRack = post(rack, Rack.class, rackService);
		Assert.assertEquals(rack, savedRack);
		
		createAndPlaceItem(savedRack, 1, 1);	//itemSlots, placementAmount
		createAndPlaceItem(savedRack, 2, 2);
		
		logger.debug("the 5 slot rack has to be full now");
		Assert.assertEquals(5, Rack.calculatePlacedSize(savedRack));
		// the rack is full now
		
		try {
			logger.debug("placing over-limit item");
			createAndPlaceItem(savedRack, 1, 1);	//itemSlots, placementAmount
			Assert.fail("should not reach");
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("validation errors"));
			Assert.assertTrue(e.getMessage().contains("Total of placement.amount * item.size must not exceed rack.place"));
			logger.debug("over-limit validation exception");
			return;
		}
	}
	
	private void createAndPlaceItem(Rack rack, Integer itemSlots,
			Integer placementAmount) throws Exception {

		logger.debug("new item with " + itemSlots + " slots, placed " + placementAmount + " times");
		
		String name = "Item*" + itemSlots;
		Item item = new Item(name, "This item needs " + itemSlots + " slot(s)", itemSlots);
		
		Item savedItem = post(item, Item.class, itemService);
		Assert.assertEquals(item, savedItem);

		Placement placement = new Placement(rack, savedItem, placementAmount, "");
		rack.getPlacements().add(placement);
		Placement savedPlacement = post(placement, Placement.class, placementService);
		Assert.assertEquals(placement, savedPlacement);
	}

	@Test
	public void testGet() throws Exception {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		Rack saved = post(rack, Rack.class, rackService);
		
		Rack retrieved = get(saved.getId(), Rack.class, rackService);
		Assert.assertEquals(rack, retrieved);
	}
	
	@Test
	public void testGetComplex() throws Exception {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		Rack savedRack = post(rack, Rack.class, rackService);
		createAndPlaceItem(savedRack, 1, 1);	//itemSlots, placementAmount
		createAndPlaceItem(savedRack, 2, 2);
		put(savedRack, Rack.class, rackService);
		
		Rack retrieved = get(savedRack.getId(), Rack.class, rackService);
		Assert.assertEquals(rack, retrieved);
	}

	
	@Test 
	public void testPut() throws Exception {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		Rack saved = post(rack, Rack.class, rackService);
		
		saved.setName("Updated Rack");
		Rack updated = put(saved, Rack.class, rackService);
		
		Assert.assertEquals(saved, updated);
		
		Rack retrieved = get(updated.getId(), Rack.class, rackService);
		Assert.assertEquals(saved, retrieved);
	}
	
	@Test
	public void testDelete() throws Exception {
		Rack rack = new Rack("Rack", "A 5 slot rack", 5);
		Rack saved = post(rack, Rack.class, rackService);
		
		int responseCode = delete(saved.getId(), Rack.class, rackService);
		Assert.assertEquals(HttpStatus.OK_200, responseCode);
	}
	
	@Test
	public void testSearch() throws Exception {
		//prepare
		{
			List<Rack> result = search("AB");
			if(result != null) {
				for(Rack rack : result) {
					delete(rack.getId(), Rack.class, rackService);
				}
			}
		}
		
		Hashtable<Long, Rack> rackReference = new Hashtable<Long, Rack>();
		Rack rack;
		
		//included
		rack = new Rack("RackAB", "A 5 slot rack", 5);
		rack = post(rack, Rack.class, rackService);
		rackReference.put(rack.getId(), rack);
		rack = new Rack("RackABC", "A 5 slot rack", 5);
		rack = post(rack, Rack.class, rackService);
		rackReference.put(rack.getId(), rack);
		
		//excluded
		post(new Rack("RackXYZ", "A 5 slot rack", 5), Rack.class, rackService);
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("name");

		List<Rack> result = search("AB");
		
		Assert.assertNotNull("results expected", result);
		Assert.assertEquals("results ammount", rackReference.size(), result.size());

		for(Rack foundRack : result) {
			Rack refRack = rackReference.get(foundRack.getId());
			Assert.assertEquals(refRack, foundRack);
		}
		
	}

	private List<Rack> search(String text) throws IOException, ClientProtocolException, Exception {
		HttpGet httpget = new HttpGet(SERVICE_URL + rackService.getPath() + "/search?text=" + text);
		httpget.setHeader("Content-Type", mimeType);
		HttpResponse response = httpclient.execute(httpget);
		List<Rack> result = readFromResponse(ArrayList.class, response);
		return result;
	}
	
	/*** post, get, put, delete, search HTTP client-server REST communication wrappers ***/

	private <T> T post(T object, Class<T> clazz, RestService service) throws Exception {
		HttpPost httppost = new HttpPost(SERVICE_URL + service.getPath());
		HttpEntity entity = new EntityTemplate(new MarshallerCP<T>(object, marshaller));
		httppost.setHeader("Content-Type", mimeType);
		httppost.setEntity(entity);
		
		HttpResponse response = httpclient.execute(httppost);
		logger.debug("status: " + response.getStatusLine().getStatusCode());
		T saved = readFromResponse(clazz, response);
		return saved;
	}

	private <T> T get(Object id, Class<T> clazz, RestService service) throws Exception {
		HttpGet httpget = new HttpGet(SERVICE_URL + service.getPath() + "/" + id);
		httpget.setHeader("Content-Type", mimeType);
		HttpResponse response = httpclient.execute(httpget);

		T retrieved = readFromResponse(clazz, response);
		return retrieved;
	}

	private <T> T put(T object, Class<T> clazz, RestService service) throws Exception {
		HttpPut httpput = new HttpPut(SERVICE_URL + service.getPath());
		HttpEntity entity = new EntityTemplate(new MarshallerCP<T>(object, marshaller));
		httpput.setHeader("Content-Type", mimeType);
		httpput.setEntity(entity);
		
		HttpResponse response = httpclient.execute(httpput);
		T updated = readFromResponse(clazz, response);
		return updated;
	}
	
	private <T> int delete(Long id, Class<T> clazz, RestService service) throws Exception {
		HttpDelete httpdelete = new HttpDelete(SERVICE_URL + service.getPath() + "/" + id);
		HttpResponse response = httpclient.execute(httpdelete);
		
		logger.debug("status: " + response.getStatusLine().getStatusCode());
		return response.getStatusLine().getStatusCode();
	}

	private <T> T readFromResponse(Class<T> clazz, HttpResponse response) throws Exception {
		InputStream is = response.getEntity().getContent();
		if(response.getStatusLine().getStatusCode() == HttpStatus.OK_200) {
			try {
				T object = marshaller.read(clazz, is);
				return object;
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		else {
			logger.debug("no entity received, but statuscode: " + response.getStatusLine().getStatusCode());
			
			StringBuilder sb = new StringBuilder();
	        String line;
	
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	            while ((line = reader.readLine()) != null) {
	                sb.append(line).append("\n");
	            }
	        } finally {
	        	is.close();
	        }
	        logger.debug("response content: " + sb.toString());
	        throw new Exception(sb.toString());
		}
	}

	private class MarshallerCP<T> implements ContentProducer {
		
		private T object;
		private RestMarshaller marshaller;
		
		
		
		public MarshallerCP(T object, RestMarshaller marshaller)  {
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
