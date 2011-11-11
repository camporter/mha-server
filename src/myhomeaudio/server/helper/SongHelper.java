package myhomeaudio.server.helper;

import java.io.File;
import java.util.StringTokenizer;

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
		String body = "";
		String header = "";
		
		StringTokenizer tokenizedUri = new StringTokenizer(this.uri, "/");
		tokenizedUri.nextToken(); // throw the first part away
		
		
		if (tokenizedUri.hasMoreTokens())
		{
			String method = tokenizedUri.nextToken();
			if (method.equals("list"))
			{
				Songs songs = Songs.getInstance();
				Gson gson = new Gson();
				
				body = gson.toJson(songs.getSongList());
				header = buildHeader(HTTP_OK, true, "application/json", body.length());
			}
			else if (method.equals("play"))
			{
				
			}
			else if (method.equals("pause"))
			{
				
			}
			
		}
		else {
			
		}
		return header + body;
	}
	


}
