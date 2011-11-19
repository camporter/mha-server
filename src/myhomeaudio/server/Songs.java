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
	ArrayList<String> songList = new ArrayList<String>(); // List of available
															// mp3 files

	protected Songs() {

	}

	/*
	 * Returns Songs instance which stores song data
	 * 
	 * @return instance Songs instance
	 */
	public static synchronized Songs getInstance() {
		if (instance == null) {
			instance = new Songs();
		}
		return instance;
	}

	/*
	 * Searches music directory and adds mp3 files to array list
	 * 
	 * @param
	 * 
	 * @return
	 */
	public void populateSongList() {
		File songDirectory = new File("music");
		
		songList = null;
		songList = new ArrayList<String>();
		
		for (String songFile : songDirectory.list()) {
			this.songList.add(songFile);
		}
		return;
	}

	/*
	 * Returns the array list of songs
	 * 
	 * @return ArrayList An array of mp3 file names available
	 */
	public ArrayList<String> getSongList() {
		return new ArrayList<String>(this.songList);
	}

	/*
	 * Reads song data into a string buffer
	 * @param songName name of the song to play
	 * @return A byte array of the mp3 music data
	 */
	public byte[] getSongData(String songName) {
		try {
			if (songExists(songName)) {
				File file = new File("music/" + songName);
				InputStream input = new FileInputStream(file);
				long length = file.length();
	
				byte[] bytes = new byte[(int) length];
	
				int offset = 0;
				int numRead = 0;
				while (offset < bytes.length
						&& (numRead = input.read(bytes, offset, bytes.length
								- offset)) >= 0) {
					offset += numRead;
				}
	
				if (offset < bytes.length) {
					throw new IOException("Didn't completely read file.");
				}
	
				input.close();
				return bytes;
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
	
	public boolean songExists(String songName) {
		this.populateSongList(); // Repopulate the list so we know it's up to date
		
		for (String song : this.songList) {
			if (songName.equals(song)) {
				return true;
			}
		}
		return false;
		
	}
}
