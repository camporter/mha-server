package myhomeaudio.server.source.youtube;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VideoMetaData {
	private String id = null;
	private String url = null;
	private String uploaded = null;
	private String uploader = null;
	private String category = null;
	private String title = null;
	private String description = null;
	private ArrayList<String> tags = null;
	private String thumbnailDefaultUrl = null;
	private String thumbnailMobileUrl = null;
	private int duration = 0; //seconds
	private String aspectRatio = null;
	private int likeCount = -1;
	private double rating = -1;
	private int ratingCount = -1;
	private int viewCount = -1;
	private int favoriteCount = -1;
	private int commentCount = -1;
	
	private String jsonString = null;

	/**
	 * VideoMetaData
	 * 	Default Constructor
	 */
	public VideoMetaData(){
		
	}
	
	/**
	 * VideoMetaData Constructor
	 * 
	 * @param data JSONC string of data.items
	 */
	public VideoMetaData(String data){
		this.jsonString = data;
		this.tags = new ArrayList<String>();
		parseJsoncObject();
	}
	
	/**
	 * parseJsoncObject
	 * 
	 * Parses the json data.items object and populates 
	 * 	VideoMetaData field
	 */
	private void parseJsoncObject(){
		JSONParser jParser = new JSONParser();
		try {
			JSONObject jData = (JSONObject)jParser.parse(jsonString);
			
			//Retrieves entry fields from object and sets class variables
			this.id = jData.keySet().toString().contains("id") ? (String)jData.get("id") : id;
			buildUrl();
			this.uploaded = jData.keySet().toString().contains("uploaded") ? (String)jData.get("uploaded") : uploaded;
			this.uploader = jData.keySet().toString().contains("uploader") ? (String)jData.get("uploader") : uploader;
			this.title = jData.keySet().toString().contains("title") ? (String)jData.get("title") : title;
			this.category = jData.keySet().toString().contains("category") ? (String)jData.get("category") : category;
			this.description = jData.keySet().toString().contains("description") ? (String)jData.get("description") : description;
			
			
			if(jData.keySet().toString().contains("tags")){
				JSONArray jArray = (JSONArray)jData.get("tags");//create json array of tag results
			
				//Places each string tag into tags arrayList
				for(int i = 0; i < jArray.size(); i++){
					this.tags.add(jArray.get(i).toString());
				}
			}
			
			//checks that keyset contains thumbnail, then that subKeyset contains hdDefault
			if(jData.keySet().toString().contains("thumbnail")){
				if(((JSONObject)jData.get("thumbnail")).keySet().toString().contains("hdDefault")){
					this.thumbnailDefaultUrl = (String)((JSONObject)jData.get("thumbnail")).get("hqDefault");
				}
			}

			//checks that keyset contains thumbnail, then that subKeyset contains sqDefault
			if(jData.keySet().toString().contains("thumbnail")){
				if(((JSONObject)jData.get("thumbnail")).keySet().toString().contains("sqDefault")){
					this.thumbnailMobileUrl = (String)((JSONObject)jData.get("thumbnail")).get("sqDefault");
				}
			}

			//Retrieves the rest of the information from the jsonc object
			this.duration = jData.keySet().toString().contains("duration") ? ResponseMetaData.parseInteger(jData.get("duration").toString()) : duration;
			this.aspectRatio = jData.keySet().toString().contains("aspectRatio") ? (String)jData.get("aspectRatio") : aspectRatio;
			this.likeCount = jData.keySet().toString().contains("likeCount") ? ResponseMetaData.parseInteger(jData.get("likeCount").toString()) : likeCount;
			this.rating = jData.keySet().toString().contains("rating") ? ResponseMetaData.parseDouble(jData.get("rating").toString()) : rating;
			this.ratingCount = jData.keySet().toString().contains("ratingCount") ? ResponseMetaData.parseInteger(jData.get("ratingCount").toString()) : ratingCount;
			this.viewCount = jData.keySet().toString().contains("viewCount") ? ResponseMetaData.parseInteger(jData.get("viewCount").toString()) : viewCount;
			this.favoriteCount = jData.keySet().toString().contains("favoriteCount") ? ResponseMetaData.parseInteger(jData.get("favoriteCount").toString()) : favoriteCount;
			this.commentCount = jData.keySet().toString().contains("commentCount") ? ResponseMetaData.parseInteger(jData.get("commentCount").toString()) : commentCount;
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(NumberFormatException e) {
			e.getMessage();
		}
	}
	
	/**
	 * buildUrl
	 * 
	 * Builds url of the video based on the video id
	 * 
	 */
	private void buildUrl(){
		String baseUrl = "http://www.youtube.com/watch?v=";
		//String baseUrl = "http://www.youtube.com/get_video?video_id=";
		this.url = baseUrl+id;
		return;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoMetaData [id=" + id + ", title=" + title
				+ ", description=" + description + ", aspectRatio="
				+ aspectRatio + ", category=" + category + ", commentCount="
				+ commentCount + ", duration=" + duration + ", favoriteCount="
				+ favoriteCount + ", likeCount=" + likeCount + ", rating="
				+ rating + ", ratingCount=" + ratingCount + ", tags="
				+ tags.toString() + ", uploaded=" + uploaded
				+ ", uploader=" + uploader + ", viewCount=" + viewCount
				+ ", thumbnailDefaultUrl=" + thumbnailDefaultUrl
				+ ", thumbnailMobileUrl=" + thumbnailMobileUrl
				+ ", url=" + url + "]";
	}

	/**
	 * @return the id
	 */
	public synchronized String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public synchronized void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the uploaded
	 */
	public synchronized String getUploaded() {
		return uploaded;
	}

	/**
	 * @param uploaded the uploaded to set
	 */
	public synchronized void setUploaded(String uploaded) {
		this.uploaded = uploaded;
	}

	/**
	 * @return the uploader
	 */
	public synchronized String getUploader() {
		return uploader;
	}

	/**
	 * @param uploader the uploader to set
	 */
	public synchronized void setUploader(String uploader) {
		this.uploader = uploader;
	}

	/**
	 * @return the category
	 */
	public synchronized String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public synchronized void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the title
	 */
	public synchronized String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public synchronized void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public synchronized String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public synchronized void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the tags
	 */
	public synchronized ArrayList<String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public synchronized void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the thumbnailDefaultUrl
	 */
	public synchronized String getThumbnailDefaultUrl() {
		return thumbnailDefaultUrl;
	}

	/**
	 * @param thumbnailDefaultUrl the thumbnailDefaultUrl to set
	 */
	public synchronized void setThumbnailDefaultUrl(String thumbnailDefaultUrl) {
		this.thumbnailDefaultUrl = thumbnailDefaultUrl;
	}

	/**
	 * @return the thumbnailMobileUrl
	 */
	public synchronized String getThumbnailMobileUrl() {
		return thumbnailMobileUrl;
	}

	/**
	 * @param thumbnailMobileUrl the thumbnailMobileUrl to set
	 */
	public synchronized void setThumbnailMobileUrl(String thumbnailMobileUrl) {
		this.thumbnailMobileUrl = thumbnailMobileUrl;
	}

	/**
	 * @return the duration
	 */
	public synchronized int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public synchronized void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the aspectRatio
	 */
	public synchronized String getAspectRatio() {
		return aspectRatio;
	}

	/**
	 * @param aspectRatio the aspectRatio to set
	 */
	public synchronized void setAspectRatio(String aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	/**
	 * @return the likeCount
	 */
	public synchronized int getLikeCount() {
		return likeCount;
	}

	/**
	 * @param likeCount the likeCount to set
	 */
	public synchronized void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	/**
	 * @return the rating
	 */
	public synchronized double getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public synchronized void setRating(double rating) {
		this.rating = rating;
	}

	/**
	 * @return the ratingCount
	 */
	public synchronized int getRatingCount() {
		return ratingCount;
	}

	/**
	 * @param ratingCount the ratingCount to set
	 */
	public synchronized void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}

	/**
	 * @return the viewCount
	 */
	public synchronized int getViewCount() {
		return viewCount;
	}

	/**
	 * @param viewCount the viewCount to set
	 */
	public synchronized void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * @return the favoriteCount
	 */
	public synchronized int getFavoriteCount() {
		return favoriteCount;
	}

	/**
	 * @param favoriteCount the favoriteCount to set
	 */
	public synchronized void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	/**
	 * @return the commentCount
	 */
	public synchronized int getCommentCount() {
		return commentCount;
	}

	/**
	 * @param commentCount the commentCount to set
	 */
	public synchronized void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	/**
	 * @return the url
	 */
	public synchronized String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public synchronized void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the jsonString
	 */
	public synchronized String getJsonString() {
		return jsonString;
	}
}
