package myhomeaudio.server;

import java.io.File;
import java.util.ArrayList;

public class Songs {
	private static Songs instance = null;
	
	protected Songs() {
		
	}
	
	public static synchronized Songs getInstance() {
		if (instance == null) {
			instance = new Songs();
		}
		return instance;
	}
	
	public ArrayList<String> getSongList()
	{
		File songDirectory = new File("music");
		ArrayList<String> songList = new ArrayList<String>();
		
		for (String songFile : songDirectory.list())
		{
			songList.add(songFile);
		}
		
		return songList;
	}
}
