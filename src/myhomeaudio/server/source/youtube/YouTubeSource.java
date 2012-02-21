package myhomeaudio.server.source.youtube;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * YouTube Interface for aggregation and compilation
 * 
 * Usage: 
 * 	1. Set search criteria or leave default ie maxResults, orderBy, or exactMatch
 * 	2. Call feedSearch() with search terms
 * 	3. Call getMediaList() to retrieve results
 * 
 * @author Ryan
 *
 */
public class YouTubeSource extends BaseSource implements Source {
	private static final String baseUrl = "http://gdata.youtube.com/feeds/api/videos?";
	private static final String alt = "jsonc";
	
	private String searchTerms = null;
	private int maxResults = 5;
	private String orderBy = OrderByCommands.RELEVANCE;
	private double version = 2.1;
	private boolean exactMatch = false; //for determining to include quotations around search terms
	
	ResponseMetaData resultsObject = null;
	

	/**
	 * Constructor for YouTube source
	 */
	//TODO Youtube only necessary for streaming audio or allow users to login to their account
	public YouTubeSource(){

	}
	

	public void feedSearch(String terms){
		//Checks that search terms not null
		if(terms.isEmpty()){
			return;
		}
		
		searchTerms = terms;
		String url = generateUrl();
		
		System.out.println("Fetching Feed From:");
		System.out.println(url);
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);

        try {
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				parseJsoncResponse(EntityUtils.toString(entity));

			}
		} catch (ClientProtocolException e) {
			System.out.println("Failure: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Failure: " + e.getMessage());
		}
	
		return;
	}

	private void parseJsoncResponse(String result){
		try{
			resultsObject  = new ResponseMetaData(result);
			System.out.println(resultsObject.toString());
		}catch(Exception e){
			e.getMessage();
		}
		
		return;
	}
	
	private String generateUrl(){
		System.out.println("Generating YouTube URL");
		
		//Construct YouTube request url
		String url = baseUrl;
		
		//Appends Search Terms
		url += "q=" + formatSearchTerms();
		
		//Appends version number
		url += "&v=" + version;
		
		//Appends OrderBy Parameter
		if(!orderBy.isEmpty()){
			url += "&orderby=" + orderBy;
		}
		
		//Appends Max Results Return
		if(maxResults != 0){
			url += "&max-results=" + maxResults;
		}
		
		//Appends Alt Parameter
		url += "&alt=" + alt;
		
		return url;
	}
	
	
	private String formatSearchTerms(){
		//Format Search Terms to be URL friendly
		//Reserved chars : / ? # [ ] @ ! $ & ' ( ) * + , ; =
		//Unreserved chars  ALPHA DIGIT - . _ ~

		char[] reserved = ":/?#[]@!$&'()*+,;=".toCharArray();
		for(char i : reserved){
			searchTerms.replace(i, '\0');
		}
		searchTerms = searchTerms.replace(' ', '+');
		
		if(exactMatch){
			searchTerms = "%22" + searchTerms + "%22"; //exact search
		}
		return searchTerms;
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



