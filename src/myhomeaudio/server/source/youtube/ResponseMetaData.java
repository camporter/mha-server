package myhomeaudio.server.source.youtube;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ResponseMetaData {
	protected static final int INVALID = -1;
	private String updated = null;
	private int totalItems = -1;
	private int startIndex = -1;
	private int itemsPerPage = -1;
	private ArrayList<VideoMetaData> items = new ArrayList<VideoMetaData>();
	
	String jsonString = null;

	/**
	 * Default ResponseMetaData Constructor
	 */
	public ResponseMetaData(){
		
	}
	
	/**
	 * ResponseMetaData Constructor
	 * 
	 * @param results JSONC response string returned from youtube
	 * @throws Exception Throws Exception if youtube returns an error message
	 */
	public ResponseMetaData(String results) throws Exception{
		this.jsonString = results;
		parseJsoncObject();
	}
	
	/**
	 * parseJsoncObject
	 * 
	 * Parses the jsonc response object and populates ResponseMetaData fields
	 * 
	 * @throws Exception Throws Exception if youtube returns an error message
	 */
	private void parseJsoncObject() throws Exception{
		JSONParser jParser = new JSONParser();

		try {
			JSONObject jObject = (JSONObject)jParser.parse(jsonString);//parse string into json object
			
			//Checks if youtube returned an error
			if(jObject.containsKey("error")){
				//Error searching
				System.out.println("Error Returned: " + jObject.get("error").toString());
				throw new Exception();
			}

			//Checks if youtube returned data
			if(jObject.keySet().toString().contains("data")){
				JSONObject data = (JSONObject)jObject.get("data");
				
				//if statements to check that youtube returned a given field before attempting to retrieve the data
				this.updated = data.keySet().toString().contains("updated") ? (String)data.get("updated") : updated;		
				this.totalItems = data.keySet().toString().contains("totalItems") ? parseInteger(data.get("totalItems").toString()) : totalItems;
				this.startIndex = data.keySet().toString().contains("startIndex") ? parseInteger(data.get("startIndex").toString()) : startIndex;
				this.itemsPerPage = data.keySet().toString().contains("itemsPerPage") ? parseInteger(data.get("itemsPerPage").toString()) : itemsPerPage;
				
				//Exit if search returned zero results
				if(totalItems == 0){
					return;
				}
				

				JSONArray jItemArray = (JSONArray)data.get("items");//create json array of item results
				
				//Create VideoMetaData object for each result and add to arrayList
				for(int i = 0; i < itemsPerPage; i++){
					jObject = (JSONObject)jItemArray.get(i);
					this.items.add(new VideoMetaData(jObject.toJSONString()));
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * toString
	 * 		Override
	 */
	@Override
	public String toString() {
		return "ResponseMetaData [updated=" + updated + ", totalItems="
				+ totalItems + ", startIndex=" + startIndex + ", itemsPerPage="
				+ itemsPerPage + ", items=" + items + "]";
	}
	
	/**
	 * parseInteger
	 * 	Parses a string into an integer
	 * @param integer String to be converted
	 * @return Returns integer value of the string, ResponseMetaData.INVALID = -1, if exception is thrown
	 */
	protected static int parseInteger(String integer){
		try{
			return Integer.parseInt(integer);
		}catch(NumberFormatException e){
			return ResponseMetaData.INVALID;
		}
	}
	
	/**
	 * parseDouble
	 * 	Parses a string into a double
	 * @param doubleValue String to be converted
	 * @return Returns double value of the string, ResponseMetaData.INVALID = -1, if exception is thrown
	 */
	protected static double parseDouble(String doubleValue){
		try{
			return Double.parseDouble(doubleValue);
		}catch(NumberFormatException e){
			return ResponseMetaData.INVALID;
		}
	}
	
	/**
	 * @return the updated
	 */
	public synchronized String getUpdated() {
		return updated;
	}
	/**
	 * @param updated the updated to set
	 */
	public synchronized void setUpdated(String updated) {
		this.updated = updated;
	}
	/**
	 * @return the totalItems
	 */
	public synchronized int getTotalItems() {
		return totalItems;
	}
	/**
	 * @param totalItems the totalItems to set
	 */
	public synchronized void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	/**
	 * @return the startIndex
	 */
	public synchronized int getStartIndex() {
		return startIndex;
	}
	/**
	 * @param startIndex the startIndex to set
	 */
	public synchronized void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	/**
	 * @return the itemsPerPage
	 */
	public synchronized int getItemsPerPage() {
		return itemsPerPage;
	}
	/**
	 * @param itemsPerPage the itemsPerPage to set
	 */
	public synchronized void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	/**
	 * @return the items
	 */
	public synchronized ArrayList<VideoMetaData> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public synchronized void setItems(ArrayList<VideoMetaData> items) {
		this.items = items;
	}
	/**
	 * @return the jsonString
	 */
	public synchronized String getJsonString() {
		return jsonString;
	}
	/**
	 * @param jsonString the jsonString to set
	 */
	public synchronized void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	
}
