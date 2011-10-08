package myhomeaudio.server.handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import myhomeaudio.server.request.NodeRequest;

public class NodeHandler extends Thread {
	private ServerSocket nodeListenSocket;
	private int nodeListenPort;
	static int numClients = 0;
	
	public NodeHandler(int port) {
		this.nodeListenSocket = null;
		this.nodeListenPort = port;
		
		try {
			nodeListenSocket = new ServerSocket(this.nodeListenPort);
		} catch (IOException e) {
			System.out.println("Unable to bind to port: " + this.nodeListenPort);
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			while (true) {
				System.out.println("Listening for nodes");

				Socket nodeSocket = nodeListenSocket.accept();
				System.out.println("Node connection Found");
				numClients++;

				NodeRequest request = new NodeRequest(nodeSocket, numClients);

				// Thread thread = new Thread(request);
				System.out.println("Starting New NodeRequest");
				request.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
