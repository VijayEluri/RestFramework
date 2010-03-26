package evs.rest.core;

import java.io.IOException;

import javax.net.ssl.SSLEngineResult.Status;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RestService extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	Class myClass = null; //analyseUrl(...);


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		Class myClass = null; //analyseUrl(...);
		
		Object id = null; //getIdFromURL
		
		Object myObject = null; //getObjectFromDB
		
		resp.setStatus(200); //TODO: HTTP.status.OK
		try {
			resp.getWriter().print("ok");  //TODO: write object JSON/XML
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
		
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
		
	}
	
	/*
	 * doHead
	 * doOptions
	 * service?

    Called by the server (via the service method) to allow a servlet to handle a GET request.
protected  void	doHead(HttpServletRequest req, HttpServletResponse resp) 
    Receives an HTTP HEAD request from the protected service method and handles the request.
protected  void	doOptions(HttpServletRequest req, HttpServletResponse resp) 
    Called by the server (via the service method) to allow a servlet to handle a OPTIONS request.
*/
}
