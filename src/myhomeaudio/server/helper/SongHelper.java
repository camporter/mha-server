package myhomeaudio.server.helper;

import java.io.File;
import org.json.JSONObject;

import com.google.gson.Gson;

import myhomeaudio.server.Songs;

public class SongHelper extends Helper implements HelperInterface {
	
	@Override
	public void setData(String uri, String body)
	{
		this.uri = uri;
		this.body = body;
	}
	
	@Override
	public String getOutput() {
		Songs songs = Songs.getInstance();
		Gson gson = new Gson();
		
		String body = gson.toJson(songs.getSongList());
		String header = buildHeader(HTTP_OK, true, "application/json", body.length());
		
		return header + body;
	}
	


}
