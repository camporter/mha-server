package myhomeaudio.server.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.StringTokenizer;

import myhomeaudio.server.http.HTTPMimeType;
import myhomeaudio.server.http.StatusCode;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

/**
 * YouTube Interface for aggregation and compilation
 * @author Ryan
 *
 */
public class YouTubeSource extends BaseSource implements Source {
	private String baseUri = "http://gdata.youtube.com/feeds/api/videos?";
	private int maxResults = 5;
	private String orderBy = "relevance";
	private String alt = "json";
	
	String ipAddress;
	

	/**
	 * Constructor for YouTube source
	 */
	//TODO Youtube only necessary for streaming audio or allow users to login to their account
	public YouTubeSource(){
		 try{
			  InetAddress ownIP=InetAddress.getLocalHost();
			  ipAddress = ownIP.getHostAddress();
			  System.out.println("IP of my system is = " + ipAddress);
			  }catch (Exception e){
			  System.out.println("Exception caught ="+e.getMessage());
			  }
	}
	
	public int feedSearch(String terms){
		
		// Set the content-type
		String contentType = HTTPMimeType.MIME_JSON;
		int httpStatus = HttpStatus.SC_BAD_REQUEST;
		
		System.out.println(terms);
		terms.replace('\"','\0');
		System.out.println(terms);
		StringTokenizer tokenizer = new StringTokenizer(terms);
		terms = terms.replace(' ', '+');
		System.out.println(terms);
		terms = "%22" + terms + "%22"; //exact search
		System.out.println(terms);
	
		
		//Construct YouTube request uri
		String uri = baseUri;
		
		//Appends Search Terms
		uri += "q=" + terms;
		
		//Appends OrderBy Parameter
		uri += "&orderby=" + orderBy;
		
		//Appends Max Results Return
		uri += "&max-results=" + maxResults;
		
		//Appends Alt Parameter
		uri += "&alt=" + alt;
		
		System.out.println(uri);
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(uri);

        try {
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity);
				JSONArray jArray = new JSONArray();			
				
				System.out.println(result);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		

		return 0;
	}
	
	public static String convertInputStreamToString(InputStream inStream){
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try{
			while((line = reader.readLine()) != null){
				sb.append(line+"\n");
			}
		}catch(IOException e){
			System.err.println(e.getMessage());
		
		} finally{
			try{
				inStream.close();
			}catch(IOException e){
				System.err.println(e.getMessage());
			}
		}
		return sb.toString();
	}
	
	@Override
	public ArrayList<String> getMediaList() {
		ArrayList<String> mediaList = new ArrayList<String>();
		File folder = null;
		
		for (String mediaFileName : folder.list()) {
			mediaList.add(mediaFileName);
		}
		return null;
	}

}
