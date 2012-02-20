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
	private static final String baseUri = "http://gdata.youtube.com/feeds/api/videos?";
	private static final String alt = "json";
	
	private String searchTerms = null;
	private int maxResults = 5;
	private String orderBy = OrderByCommands.RELEVANCE;
	private boolean exactMatch = false; //for determining to include quotations around search terms
	
	ArrayList<VideoMetaData> resultsArray;
	

	/**
	 * Constructor for YouTube source
	 */
	//TODO Youtube only necessary for streaming audio or allow users to login to their account
	public YouTubeSource(){
		resultsArray = new ArrayList<VideoMetaData>();

	}
	

	public void feedSearch(String terms){
		//Checks that search terms not null
		if(terms.isEmpty()){
			return;
		}
		
		searchTerms = terms;
		String uri = generateUri();
		
		System.out.println("Fetching Feed From:");
		System.out.println(uri);
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(uri);

        try {
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity);
				parseJSON(result);

			}
		} catch (ClientProtocolException e) {
			System.out.println("Failure: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Failure: " + e.getMessage());
		}

		return;
	}

	private void parseJSON(String result){
		JSONParser jParse = new JSONParser();
		JSONObject returnedResults=  new JSONObject();

		try {
			returnedResults = (JSONObject)jParse.parse(result);
			
			/* Keys from initial JSON Object
			 * logo = .gif, link = schemas, openSearch$totalResults = int value of totalResults
			 * xmlns$media = yahoo.com/mrss, xmlns = w3.org/2005/atom, xmlns$app = atom/app
			 * id = feeds/api/videos, xmlns$openSearch = spec/opensearchrss/1.0/, author = youtube
			 * xmlns$gd = schemas.google.com/g/2005, category = scheme, title = query term match
			 * openSearch$startIndex = start index, updated = timestamp, xmlns$yt = gdata.youtube.com/schemas
			 * openSearch$itemsPerPage = number of items per page, generator = data api
			 * 
			 */
			
			//Parses feed key from json
			//Result Entry data returned within feed.entry JSONArray
			returnedResults = (JSONObject)returnedResults.get("feed");
			//System.out.println(returnedResults.keySet().toString());
			JSONArray entryResults = (JSONArray)returnedResults.get("entry");
			
			//Creates an array of entries
			// = (JSONArray)jParse.parse(returnedResults.toString());
			
			/* Keys from feed.entry
			 * app$control = syndication and restriction, link = some link, yt$statistics = counts, id = gdata/feeds/api stuff
			 * author = uploaders, category = schema, updated = timestamp
			 * gd$rating = #raters and rating, published = published date, gd$comments = countHits, title = title
			 * media$group has some information
			 * 
			 */
			
			
			for(int i = 0; i < maxResults; i++){
				VideoMetaData mediaResult = new VideoMetaData((JSONObject)entryResults.get(i));
				this.resultsArray.add(mediaResult);
				//System.out.println(i + " " + mediaResult.toString());
				
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
		
	}
	private String generateUri(){
		System.out.println("Generating YouTube URL");
		
		//Construct YouTube request uri
		String uri = baseUri;
		
		//Appends Search Terms
		uri += "q=" + formatSearchTerms();
		
		//Appends OrderBy Parameter
		uri += "&orderby=" + orderBy;
		
		//Appends Max Results Return
		uri += "&max-results=" + maxResults;
		
		//Appends Alt Parameter
		uri += "&alt=" + alt;
		
		return uri;
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
