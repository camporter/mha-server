package myhomeaudio.server.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.songs.SongFiles;

/**
 * NodeWorker is a thread that sends commands to nodes and receives
 * confirmation.
 * 
 * @author Cameron
 * 
 */
public class NodeWorker extends Thread implements HTTPStatus, HTTPMimeType,
		NodeCommands {
	int command = -1;
	String ipAddress;
	String data;

	public NodeWorker() {

	}

	/**
	 * Initializes NodeWorker to handle request to node
	 * @param command
	 * 		Command for server to execute to node
	 * @param ipAddress
	 * 		Address of node
	 * @param data
	 * 		Data to be sent to the node
	 */
	synchronized public void setRequestData(int command, String ipAddress,
			String data) {
		this.command = command;
		this.ipAddress = ipAddress;
		this.data = data;
		notify();
	}

	synchronized public void run() {
		if (this.command == -1) {
			// request data hasn't been set yet
			System.out.println("NodeWorker: Request data hasn't been set");
			try {
				// put the thread to sleep
				wait();
			} catch (InterruptedException e) {
				// ???
			}
		}
		try {
			Socket socket = new Socket(this.ipAddress, 9091);
			String output = "";
			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			DataOutputStream outputStream = new DataOutputStream( 
					socket.getOutputStream());

			switch (command) {
			case NODE_PLAY:
				SongFiles s = SongFiles.getInstance(); //Gets song list
				System.out.println("Song instance " + this.data);
				byte[] songData = s.getSongData(this.data); //Gets byte[] of mp3 data
				outputStream.writeBytes(HTTPHeader.buildRequest("POST", "play", true,
						MIME_MP3, songData.length));//Puts songdata in http request
				
				outputStream.write(songData);
				break;
			case NODE_PAUSE:
				outputStream.writeBytes(HTTPHeader.buildRequest("GET", "pause", false, "", 0));
				break;
			}

			socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
