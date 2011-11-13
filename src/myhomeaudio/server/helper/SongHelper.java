package myhomeaudio.server.helper;

import java.io.File;
import java.util.StringTokenizer;

import com.google.gson.Gson;

import myhomeaudio.server.NodeManager;
import myhomeaudio.server.Songs;
import myhomeaudio.server.http.HTTPHeader;
import myhomeaudio.server.http.HTTPMimeType;
import myhomeaudio.server.node.NodeCommands;

public class SongHelper extends Helper implements HelperInterface, HTTPMimeType, NodeCommands {
	
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
		tokenizedUri.nextToken(); // throw the first part away, throws /song away
		
		
		if (tokenizedUri.hasMoreTokens())
		{
			//Possible methods: list, play, pause
			String method = tokenizedUri.nextToken();
			if (method.equals("list"))
			{
				System.out.println("SongHelper List");
				Songs songs = Songs.getInstance();
				Gson gson = new Gson();
				
				body = gson.toJson(songs.getSongList());
				System.out.println(body);
				header = HTTPHeader.buildResponse(HTTP_OK, true, MIME_JSON, body.length());
				System.out.println(header);
			}
			else if (method.equals("play"))
			{
				//TODO need to know ipaddress of node to send data to
				NodeManager nm = NodeManager.getInstance();
				nm.sendNodeCommand(NODE_PLAY, " ");
			}
			else if (method.equals("pause"))
			{
				NodeManager nm = NodeManager.getInstance();
				nm.sendNodeCommand(NODE_PAUSE, "");
			}
			
		}
		else {
			
		}
		return header + body;
	}
	


}
