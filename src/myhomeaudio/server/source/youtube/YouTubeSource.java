package myhomeaudio.server.source.youtube;

import java.io.IOException;

import myhomeaudio.server.source.LocationSource;

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
 * Usage: 1. Set search criteria or leave default ie maxResults, orderBy, or
 * exactMatch 2. Call feedSearch() with search terms 3. Call getMediaList() to
 * retrieve results
 * 
 * @author Ryan
 * 
 */
public class YouTubeSource extends LocationSource {

	private static final String BASE_URL = "http://gdata.youtube.com/feeds/api/videos?";
	private static final String alt = "jsonc";
	private static final double version = 2.1;

	private String searchTerms = null;
	private int maxResults = 5;
	private String orderBy = OrderByCommands.RELEVANCE;
	private boolean exactMatch = false; // for determining to include quotations
										// around search terms

	// Need to format in standard way as FolderSource
	ResponseMetaData resultsObject = null;

	/**
	 * Constructor for YouTube source
	 */
	// TODO Youtube only necessary for streaming audio or allow users to login
	// to their account
	public YouTubeSource() {

	}

	/**
	 * feedSearch() Searches youtube for results using the given search terms
	 * 
	 * @param terms
	 *            Youtube video search terms
	 */
	public void feedSearch(String terms) {
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

		// System.out.println(searchTerms);

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

	/**
	 * @param maxResults
	 *            the maxResults to set
	 */
	public synchronized void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public synchronized void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @param exactMatch
	 *            the exactMatch to set
	 */
	public synchronized void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}

	/**
	 * @return the resultsObject
	 */
	public synchronized ResponseMetaData getResultsObject() {
		return resultsObject;
	}

}
