package myhomeaudio.server.source.youtube;
//TODO need to check key is available
import java.util.ArrayList;
import java.util.Arrays;

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
	private int likeCount = 0;
	private double rating = 0;
	private int ratingCount = 0;
	private int viewCount = 0;
	private int favoriteCount = 0;
	private int commentCount;
	
	private String jsonString = null;

	public VideoMetaData(){
		
	}
	
	public VideoMetaData(String data){
		this.jsonString = data;
		this.tags = new ArrayList<String>();
		parseJsoncObject();
	}
	
	private void parseJsoncObject(){
		JSONParser jParser = new JSONParser();
		try {
			JSONObject jData = (JSONObject)jParser.parse(jsonString);
			this.id = (String)jData.get("id");
			buildUrl();
			this.uploaded = (String)jData.get("uploaded");
			this.uploader = (String)jData.get("uploader");
			this.title = (String)jData.get("title");
			this.category = (String)jData.get("category");
			this.description = (String)jData.get("description");
			JSONArray jArray = (JSONArray)jData.get("tags");
		
			for(int i = 0; i < jArray.size(); i++){
				this.tags.add(jArray.get(i).toString());
			}

			this.thumbnailDefaultUrl = (String)((JSONObject)jData.get("thumbnail")).get("hqDefault");
			this.thumbnailMobileUrl = (String)((JSONObject)jData.get("thumbnail")).get("sqDefault");
			this.duration = ResponseMetaData.parseInteger(jData.get("duration").toString());
			this.aspectRatio = (String)jData.get("aspectRatio");
			this.likeCount = ResponseMetaData.parseInteger(jData.get("likeCount").toString());
			this.rating = ResponseMetaData.parseDouble(jData.get("rating").toString());
			this.ratingCount = ResponseMetaData.parseInteger(jData.get("ratingCount").toString());
			this.viewCount = ResponseMetaData.parseInteger(jData.get("viewCount").toString());
			this.favoriteCount = ResponseMetaData.parseInteger(jData.get("favoriteCount").toString());
			//System.out.println("ddd");
			System.out.println(jData.get("commentCount").toString());
			//System.out.println("dqw");
			this.commentCount = ResponseMetaData.parseInteger(jData.get("commentCount").toString());
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(NumberFormatException e) {
			e.getMessage();
		}
	}
	
	private void buildUrl(){
		String baseUrl = "http://www.youtube.com/watch?v=";
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
