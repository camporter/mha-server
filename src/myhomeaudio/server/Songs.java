package myhomeaudio.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Songs {
	private static Songs instance = null;
	ArrayList<String> songList = new ArrayList<String>(); //List of available mp3 files
	
	protected Songs() {
		
	}
	
	/* Returns Songs instance which stores song data
	 * 
	 * @return instance
	 * 				Songs instance
	 * 
	 */
	public static synchronized Songs getInstance() {
		if (instance == null) {
			instance = new Songs();
		}
		return instance;
	}
	
	/* Searches music directory and adds mp3 files to array list
	 * 
	 * @param
	 * @return
	 */
	public void populateSongList() {
		File songDirectory = new File("music");
		
		
		for (String songFile : songDirectory.list())
		{
			this.songList.add(songFile);
		}
		return;
	}
	
	/* Returns the array list of songs
	 * 
	 * @return ArrayList
	 * 		An array of mp3 file names available 
	 * 
	 */
	public ArrayList<String> getSongList()
	{
		return new ArrayList<String>(this.songList);
	}
	public String getSongData() {
		try {
			FileReader songFile = new FileReader("music/"+songList.get(0));
			
			StringBuffer songData = new StringBuffer(2000);
			BufferedReader reader = new BufferedReader(songFile);
			//char[] buf = 1024;
			char[] buf = new char[1024];
			int numRead = 0;
			while((numRead=reader.read(buf)) != -1) {
				songData.append(buf, 0, numRead);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
