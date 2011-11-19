package myhomeaudio.server.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import myhomeaudio.server.Songs;
import myhomeaudio.server.node.NodeCommands;

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
				Songs s = Songs.getInstance();
				byte[] songData = s.getSongData();
				outputStream.writeBytes(HTTPHeader.buildRequest("POST", "play", true,
						MIME_MP3, songData.length));
				
				outputStream.write(songData);
				break;
			case NODE_PAUSE:
				outputStream.writeBytes(HTTPHeader.buildRequest("GET", "pause", false, "", 0));
				break;
			}

			//outputStream.writeBytes(output);

			socket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
