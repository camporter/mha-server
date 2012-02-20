package myhomeaudio.server.source.youtube;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class VideoMetaData {
	private String title = null;
	private String url = null;
	private String description = null;
	private float viewCount = 0;
	private int favoriteCount = 0;
	private float averageRating = 0;
	private int numRaters = 0;
	private int duration = 0; //seconds
	
	public VideoMetaData(JSONObject mediaInfo){
		//title = extractTitleFromJson((JSONObject)mediaInfo.get("title"));
		//title = (String)((JSONObject)mediaInfo.get("title")).get("$t");
		extractTitleFromJson((JSONObject)mediaInfo.get("title"));
		extractStatisticsFromJson((JSONObject)mediaInfo.get("yt$statistics"));
		extractRatingFromJson((JSONObject)mediaInfo.get("gd$rating"));
		extractMediaGroupFromJson((JSONObject)mediaInfo.get("media$group"));		
	}

	private void extractMediaGroupFromJson(JSONObject mediaGroup) {
		JSONArray jArray = (JSONArray)mediaGroup.get("media$content");
		this.duration = Integer.parseInt(((JSONObject) jArray.get(0)).get("duration").toString());
		this.url = ((JSONObject)jArray.get(0)).get("url").toString();
		this.description = ((JSONObject)mediaGroup.get("media$description")).get("$t").toString();
	}

	private void extractTitleFromJson(JSONObject title){
		this.title = (String) title.get("$t");
	}
	private void extractStatisticsFromJson(JSONObject statistics){
		this.favoriteCount = Integer.parseInt(statistics.get("favoriteCount").toString());
		this.viewCount = Float.parseFloat((statistics.get("viewCount").toString()));
	}
	private void extractRatingFromJson(JSONObject rating){
		this.averageRating = Float.parseFloat(rating.get("average").toString());
		this.numRaters = Integer.parseInt(rating.get("numRaters").toString());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VideoMedia [averageRating=" + averageRating + ", description="
				+ description + ", duration=" + duration + ", favoriteCount="
				+ favoriteCount + ", numRaters=" + numRaters + ", title="
				+ title + ", url=" + url + ", viewCount=" + viewCount + "]";
	}
	

}
