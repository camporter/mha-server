package myhomeaudio.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import myhomeaudio.server.helper.Helper;
import myhomeaudio.server.helper.NodeHelper;
import myhomeaudio.server.helper.SongHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.util.EntityUtils;

/**
 * Worker thread that handles incoming http requests
 * 
 * @author grimmjow
 *
 */
public class Worker {
	
	protected final String rootDirectory = "/";
	
	//http parameters and services
	private final HttpParams params;
	private final HttpService httpService;
	
	public Worker()	{
		//Initialize http parameters
        this.params = new SyncBasicHttpParams();
        this.params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        this.params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024);
        this.params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
        this.params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);
        this.params.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "Server: My Home Audio");
        
        //Create interceptor array that will handle the response
        HttpResponseInterceptor[] httpResponseInter = new HttpResponseInterceptor[]{
        		new ResponseDate(),
                new ResponseServer(),
                new ResponseContent(),
                new ResponseConnControl()
        	};
        
        //Create HttpProcessor to store interceptors
        HttpProcessor httpProcessor = new ImmutableHttpProcessor(httpResponseInter);
        
        //Create registry that stores key used to process request URI
        HttpRequestHandlerRegistry httpRequestReqistry = new HttpRequestHandlerRegistry();
        httpRequestReqistry.register("*", new HttpFileHandler());
        
        //Create http service that implements the http precessor and uses
        //the interceptors to handle http requests
        this.httpService = new HttpService(
        		httpProcessor,
        		new DefaultConnectionReuseStrategy(),
        		new DefaultHttpResponseFactory(),
        		httpRequestReqistry,
        		this.params);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static class HttpFileHandler implements HttpRequestHandler  {
	        
	    public HttpFileHandler() {
	    	super();
	    }
	        
	    public void handle(
	    	final HttpRequest request,
	        final HttpResponse response,
	        final HttpContext context) throws HttpException, IOException {
	
	     	//Checks that request method is supported
	        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
	        if (!method.equals("GET") && !method.equals("POST")) {
	        	throw new MethodNotSupportedException(method + " method not supported"); 
	        }
	            
	        //Retrieves request URI from HTTP message
	        String requestUri = request.getRequestLine().getUri();
	        String httpBody = null;
	
	        // POST methods contain data, so we need to put it in httpBody
	        if(method.equals("POST")){
	        	//Checks that request matches entityRequest
	          	//if so, retrieve data out of the request entity
	          	//else type is unknown, discard and ignore
	          	if(request instanceof HttpEntityEnclosingRequest){
	           		HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
	           		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
	           		StringBuilder stringBuilder = new StringBuilder();
	           		String line;
	           		while ((line = reader.readLine()) != null) {
						stringBuilder.append(line + "\n");
	           		}
	           		httpBody = stringBuilder.toString();
	           		System.out.println("Worker: stringbuilder");
	           		System.out.println(httpBody);
	           	}
	        }
	           
	        Helper currentHelper;
	        StringTokenizer tokenizedUri = new StringTokenizer(requestUri, "/");
			try {
				currentHelper = getCorrectHelper(tokenizedUri.nextToken());
		
			} catch (NoSuchElementException e) {
				// Client didn't specify a helper.
				// Do something else here
				currentHelper = new Helper();
			}
			
			// Give the helper the URI and body
			currentHelper.setData(requestUri, httpBody);
			
			// Get back the output it has generated
			String output = currentHelper.getOutput();
			
			//Set response status code and entity body
			response.setStatusCode(currentHelper.getStatusCode());
			StringEntity stringEntity = new StringEntity(output);
			response.setEntity(stringEntity);
			
			// Clean up stuff
			requestUri = null;
			output = null;
	        }
	    }
			
		/* Uses root of URI directory to choose correct Helper to handle request
		 * 	/node/list   <- NodeHelper
		 *  /song/list   <- SongHelper
		 * 
		 * @param helperName
		 * 		Name of the Helper class required to handle the request
		 * 
		 * @return Helper
		 * 		Returns the specified Helper class
		 */
		private static Helper getCorrectHelper(String helperName) {
			if (helperName.equals("node")) {
				System.out.println("Creating NodeHelper");
				return new NodeHelper();
			} else if (helperName.equals("song")) {
				System.out.println("Creating SongHelper");
				return new SongHelper();
			} else {
				// default
				return new Helper();
			}
		}
	

	/**
	 * @return the params
	 */
	public HttpParams getParams() {
		return params;
	}

	/**
	 * @return the httpService
	 */
	public HttpService getHttpService() {
		return httpService;
	}

}
