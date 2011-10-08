package myhomeaudio.server.handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import myhomeaudio.server.node.NodeRequest;

/**
 * NodeHandler runs as a thread, waiting for nodes to connect through its loop.
 * 
 * @author cameron
 * 
 */
public class NodeHandler extends Thread {
	private ServerSocket nodeListenSocket; // Socket to use for listening
	private int nodeListenPort; // Port to listen on
	private int numNodes = 0; // Number of nodes that have connected

	public NodeHandler(int port) {
		this.nodeListenSocket = null;
		this.nodeListenPort = port;

		try {
			nodeListenSocket = new ServerSocket(this.nodeListenPort);
		} catch (IOException e) {
			System.out
					.println("NodeHandler: Unable to bind to port: " + this.nodeListenPort);
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while (true) {
				System.out.println("Listening for nodes");

				if (this.nodeListenSocket == null) {
					return; // Stop this thread if the socket isn't available
				}

				Socket nodeSocket = this.nodeListenSocket.accept();
				System.out.println("Node connection Found");
				this.numNodes++;
				
				// Give the request its own thread
				NodeRequest request = new NodeRequest(nodeSocket, numNodes);

				System.out.println("Starting New NodeRequest");
				request.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("NodeHandler exited!");
			return;
		}
	}
}