package myhomeaudio.server.stream;

import java.util.ArrayList;

import myhomeaudio.server.manager.StreamManager;

/**
 * Updates each individual Stream object while the server runs.
 * @author Cameron
 *
 */
public class StreamThread extends Thread {
	
	StreamManager streamManager;
	ArrayList<StreamBase> streamList;
	
	public StreamThread() {
		streamManager = StreamManager.getInstance(this);
	}
	
	/**
	 * Grabs the 
	 * @param list
	 */
	public void setStreamList(ArrayList<StreamBase> list) {
		streamList = list;
	}
	
	
	public void run() {
		while (true) {
			if (streamList != null) {
				
			}
		}
	}
	
}
