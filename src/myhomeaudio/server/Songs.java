package myhomeaudio.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
	
	/* Reads song data into a string buffer
	 * 
	 * @return songdata
	 * 			StringBuffer of mp3 music data
	 * 
	 */
	/*public String getSongData() {
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
			
			return songData.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}*/
	
	public byte[] getSongData() {
		try {
			File file = new File("music/"+songList.get(0));
			InputStream input = new FileInputStream(file);
			long length = file.length();
			
			byte[] bytes = new byte[(int) length];
			
			int offset = 0;
			int numRead = 0;
			while(offset < bytes.length && (numRead=input.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}
			
			if (offset < bytes.length)
			{
				throw new IOException("Didn't completely read file.");
			}
			
			input.close();
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
}
