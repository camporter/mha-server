package myhomeaudio.server.helper;

import java.util.Hashtable;
import java.util.StringTokenizer;

import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.songs.SongFiles;

import org.apache.http.HttpStatus;

import com.google.gson.Gson;

public class SongHelper extends Helper implements HelperInterface, NodeCommands {
	
	@Override
	public void setData(String uri, String data)
	{
		this.uri = uri;
		this.data = data;
		
	}
	
	@Override
	public String getOutput() {
		String body = "";
		
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
				this.statusCode = HttpStatus.SC_OK;
			}
			else if (method.equals("play"))
			{
				Gson gson = new Gson();
				Hashtable hasht = gson.fromJson(this.data.trim(), Hashtable.class);
				
				this.statusCode = HttpStatus.SC_OK;
				//TODO need to know ipaddress of node to send data to
				NodeManager nm = NodeManager.getInstance();
				nm.sendNodeCommand(NODE_PLAY, hasht.get("song").toString());
				//nm.sendNodeCommand(NODE_PLAY, "Buffalo For What.mp3");
			}
			else if (method.equals("pause"))
			{
				this.statusCode = HttpStatus.SC_OK;
				NodeManager nm = NodeManager.getInstance();
				nm.sendNodeCommand(NODE_PAUSE, "");
			}
			
		}
		else {
			
		}
		return body;
	}
	


}
