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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	public ResponseMetaData(){
		
	}
	public ResponseMetaData(String results) throws Exception{
		this.jsonString = results;
		parseJsoncObject();
	}
	private void parseJsoncObject() throws Exception{
		JSONParser jParser = new JSONParser();

		try {
			JSONObject jObject = (JSONObject)jParser.parse(jsonString);
			
			if(jObject.containsKey("error")){
				throw new Exception();
			}
			
			JSONObject data = (JSONObject)jObject.get("data");
			this.updated = (String)data.get("updated");
			this.totalItems = parseInteger(data.get("totalItems").toString());
			this.startIndex = parseInteger(data.get("startIndex").toString());
			this.itemsPerPage = parseInteger(data.get("itemsPerPage").toString());
			
			if(totalItems == 0){
				return;
			}
			
			JSONArray jItemArray = (JSONArray)data.get("items");
			
			for(int i = 0; i < itemsPerPage; i++){
				jObject = (JSONObject)jItemArray.get(i);
				this.items.add(new VideoMetaData(jObject.toJSONString()));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResponseMetaData [updated=" + updated + ", totalItems="
				+ totalItems + ", startIndex=" + startIndex + ", itemsPerPage="
				+ itemsPerPage + ", items=" + items + "]";
	}
	
	protected static int parseInteger(String integer){
		try{
			System.out.println(integer);
			return Integer.parseInt(integer);
		}catch(NumberFormatException e){
			return ResponseMetaData.INVALID;
		}
	}
	
	protected static double parseDouble(String integer){
		try{
			return Double.parseDouble(integer);
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
