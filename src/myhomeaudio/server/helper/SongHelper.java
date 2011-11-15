package myhomeaudio.server.helper;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.google.gson.Gson;

import myhomeaudio.server.http.HTTPHeader;
import myhomeaudio.server.http.HTTPMimeType;
import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.songs.SongFiles;

public class SongHelper extends Helper implements HelperInterface, HTTPMimeType, NodeCommands {
	
	@Override
	public void setData(String uri, String data)
	{
		this.uri = uri;
		this.data = data;
		
	}
	
	@Override
	public String getOutput() {
		String body = "";
		String header = "";
		
		//System.out.println(this.uri);
		//System.out.println(this.data);
		
		StringTokenizer tokenizedUri = new StringTokenizer(this.uri, "/");
		tokenizedUri.nextToken(); // throw the first part away, throws /song away
		
		
		if (tokenizedUri.hasMoreTokens())
		{
			//Possible methods: list, play, pause
			String method = tokenizedUri.nextToken(); //NoSuchElementException
			if (method.equals("list"))
			{
				SongFiles songs = SongFiles.getInstance();
				Gson gson = new Gson();
				
				body = gson.toJson(songs.getSongList());
				header = HTTPHeader.buildResponse(HTTP_OK, true, MIME_JSON, body.length());
			}
			else if (method.equals("play"))
			{
				Gson gson = new Gson();
				Hashtable hasht = gson.fromJson(this.data.trim(), Hashtable.class);
				
				header = HTTPHeader.buildResponse(HTTP_OK, false, "", 0);
				//TODO need to know ipaddress of node to send data to
				NodeManager nm = NodeManager.getInstance();
				nm.sendNodeCommand(NODE_PLAY, hasht.get("song").toString());
				//nm.sendNodeCommand(NODE_PLAY, "Buffalo For What.mp3");
			}
			else if (method.equals("pause"))
			{
				header = HTTPHeader.buildResponse(HTTP_OK, false, "", 0);
				NodeManager nm = NodeManager.getInstance();
				nm.sendNodeCommand(NODE_PAUSE, "");
			}
			
		}
		else {
			
		}
		return header + body;
	}
	


}
