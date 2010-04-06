package evs.rest.core.util;

import evs.rest.core.marshal.RestFormat;

public abstract class RestConst {

	public static final String DEFAULT_SERVICE_PATH = "/";
	public static final RestFormat[] DEFAULT_FORMATS = {RestFormat.JSON,RestFormat.XML};
	public static final Class<Object> DEFAULT_ID_CLASS = (Class)Long.class;
	public static final int DEFAULT_SERVER_PORT = 8777;
	public static final String DEFAULT_SEARCH_PATH = "search";

}
