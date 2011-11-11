package myhomeaudio.server.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * NodeWorker is a thread that sends commands to nodes and receives
 * confirmation.
 * 
 * @author Cameron
 * 
 */
public class NodeWorker extends Thread implements HTTPStatus, HTTPMimeType {
	int command = -1;
	String ipAddress;
	String data;
	
	public NodeWorker() {
		
	}
	synchronized public void setRequestData(int command, String ipAddress, String data)
	{
		this.command = command;
		this.ipAddress = ipAddress;
		this.data = data;
		notify();
	}
	synchronized public void run() {
		if (this.command == -1)
		{
			// request data hasn't been set yet
			try {
				// put the thread to sleep
				wait();
			} catch (InterruptedException e) {
				// ???
			}
		}
		try {
			Socket socket = new Socket(this.ipAddress, 9090);
			
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			
			socket.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
