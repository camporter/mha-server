package myhomeaudio.server.source.youtube;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import myhomeaudio.server.source.BaseSource;
import myhomeaudio.server.source.Source;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * YouTube Interface for aggregation and compilation
 * @author Ryan
 *
 */
public class YouTubeSource extends BaseSource implements Source {
	private static final String baseUri = "http://gdata.youtube.com/feeds/api/videos?";
	private static final String alt = "json";
	
	private int maxResults = 5;
	private String orderBy = OrderBy.RELEVANCE;
	private boolean exactMatch = false; //for determining to include quotations around search terms
	

	/**
	 * Constructor for YouTube source
	 */
	//TODO Youtube only necessary for streaming audio or allow users to login to their account
	public YouTubeSource(){
	}
	

	public int feedSearch(String terms){
		
		System.out.println("Generating YouTube URL");
		
		//Format Search Terms to be URL friendly
		terms.replace('\"','\0');
		terms = terms.replace(' ', '+');
		
		if(exactMatch){
			terms = "%22" + terms + "%22"; //exact search
		}
		
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
		
		System.out.println("Fetching Feed From:");
		System.out.println(uri);
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(uri);

        try {
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity);
				System.out.println("Success: Results Returned");
				System.out.println(result);
			}
		} catch (ClientProtocolException e) {
			System.out.println("Failure: No Results Returned");
		} catch (IOException e) {
			System.out.println("Failure: No Results Returned");
		}

		return 0;
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

	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @param exactMatch the exactMatch to set
	 */
	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}

}
