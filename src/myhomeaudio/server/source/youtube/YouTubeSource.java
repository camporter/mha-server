package myhomeaudio.server.source.youtube;

import java.io.IOException;
import java.util.ArrayList;

import myhomeaudio.server.media.descriptor.MediaDescriptor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * YouTube Interface for aggregation and compilation
 * 
 * 
 * @author Ryan
 * 
 * 
 */
public class YouTubeSource {

	private static final String BASE_URL = "http://gdata.youtube.com/feeds/api/videos?";
	private static final String alt = "jsonc";
	private static final double version = 2.1;
	
	//Default search values
	private static final int defaultMaxResults = 5;
	private static final boolean defaultExactMatch = false;
	private static final String defaultOrderBy = OrderByCommands.RELEVANCE;

	private String searchTerms = null;
	private int maxResults;
	private String orderBy;
	private boolean exactMatch; // for determining to include quotations
										// around search terms

	// Need to format in standard way as FolderSource
	ResponseMetaData resultsObject = null;
	

	/**
	 * Constructor for YouTube source
	 */
	// TODO Youtube only necessary for streaming audio or allow users to login
	// to their account
	public YouTubeSource() {
		this.maxResults = defaultMaxResults;
		this.orderBy = defaultOrderBy;
		this.exactMatch = defaultExactMatch;
	}
	
	public YouTubeSource(int maxResults, String orderBy, boolean exactMatch){
		this.maxResults = maxResults;
		this.orderBy = orderBy;
		this.exactMatch = exactMatch;
	}
	
	public YouTubeSource(int maxResults, String orderBy){
		this.maxResults = maxResults;
		this.orderBy = orderBy;
		this.exactMatch = defaultExactMatch;
		
	}
	
	public YouTubeSource(int maxResults, boolean exactMatch){
		this.maxResults = maxResults;
		this.orderBy = defaultOrderBy;
		this.exactMatch = exactMatch;
		
	}
	
	public YouTubeSource(String orderBy, boolean exactMatch){
		this.maxResults = defaultMaxResults;
		this.orderBy = orderBy;
		this.exactMatch = exactMatch;
		
	}
	
	public YouTubeSource(int maxResults){
		this.maxResults = maxResults;
		this.orderBy = defaultOrderBy;
		this.exactMatch = defaultExactMatch;
		
	}
	
	public YouTubeSource(String orderBy){
		this.maxResults = defaultMaxResults;
		this.orderBy = orderBy;
		this.exactMatch = defaultExactMatch;
	}
	
	public YouTubeSource(boolean exactMatch){
		this.maxResults = defaultMaxResults;
		this.orderBy = defaultOrderBy;
		this.exactMatch = exactMatch;
	}

	/**
	 * feedSearch() Searches youtube for results using the given search terms
	 * 
	 * @param terms
	 *            Youtube video search terms
	 */
	private void feedSearch(String terms) {
		// Checks that search terms not null
		if (terms.isEmpty()) {
			return;
		}

		searchTerms = terms;
		String url = generateUrl();// constructs search url

		System.out.println("Fetching Feed From:");
		System.out.println(url);

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);

		try {
			HttpResponse response = httpClient.execute(httpget);// executes url
																// and waits for
																// response
			HttpEntity entity = response.getEntity();

			// checks response is not empty
			if (entity != null) {
				parseJsoncResponse(EntityUtils.toString(entity));

			}

		} catch (ClientProtocolException e) {
			System.out.println("Failure: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Failure: " + e.getMessage());
		}

		return;
	}

	/**
	 * parseJsoncResponse()
	 * 
	 * @param result
	 *            JSONC response string returned by youtube
	 */
	private void parseJsoncResponse(String result) {
		try {
			resultsObject = new ResponseMetaData(result);
			System.out.println(resultsObject.toString());
		} catch (Exception e) {
			e.getMessage();
		}

		return;
	}

	private String generateUrl() {
		System.out.println("Generating YouTube URL");

		// Construct YouTube request url
		String url = BASE_URL;

		// Appends Search Terms
		url += "q=" + formatSearchTerms();

		// Appends version number
		url += "&v=" + version;

		// Appends OrderBy Parameter
		if (!orderBy.isEmpty()) {
			url += "&orderby=" + orderBy;
		}

		// Appends Max Results Returned
		if (maxResults != 0) {
			url += "&max-results=" + maxResults;
		}

		// Appends Alt Parameter
		url += "&alt=" + alt;

		return url;
	}

	private String formatSearchTerms() {
		// Format Search Terms to be URL friendly
		// Reserved chars : / ? # [ ] @ ! $ & ' ( ) * + , ; =
		// Unreserved chars ALPHA DIGIT - . _ ~

		// Illegal characters for url
		char[] reserved = ":/?#[]@!$&'()*+,;=%^[]\\|".toCharArray();

		// Exchanges illegal url characters for spaces
		for (char i : reserved) {
			// System.out.println(i+ " " + searchTerms);
			searchTerms = searchTerms.replace(i, ' ');
		}

		// Removes multiple consecutive spaces
		for (int i = 0; i < searchTerms.length(); i++) {
			// System.out.println(" " + searchTerms);
			searchTerms = searchTerms.replace("  ", " ");
		}

		// Converts spaces into search appending '+'
		searchTerms = searchTerms.replace(' ', '+');

		// If exact match search, append \" or %22 to beginning and end of
		// string
		if (exactMatch) {
			searchTerms = "%22" + searchTerms + "%22"; // exact search
		}
		
		return searchTerms;
	}

	/*public ArrayList<String> getMediaList() {
		ArrayList<String> mediaList = new ArrayList<String>();
		File folder = null;

		for (String mediaFileName : folder.list()) {
			mediaList.add(mediaFileName);
		}
		return null;
	}*/
	
	
	public ArrayList<MediaDescriptor> searchMedia(String search) {
		feedSearch(search);
		return createMediaDescriptor();
	}
	
	private ArrayList<MediaDescriptor> createMediaDescriptor(){
		ArrayList<VideoMetaData> videoMetaData = resultsObject.getItems();
		ArrayList<MediaDescriptor> mediaDesc = new ArrayList<MediaDescriptor>();
		VideoMetaData data = null;
		
		while(!videoMetaData.isEmpty()){
			data = videoMetaData.remove(0);
			mediaDesc.add(new MediaDescriptor(0,data.getTitle(),"","","",data.getUrl()));
		}
		return mediaDesc;
	}

}
