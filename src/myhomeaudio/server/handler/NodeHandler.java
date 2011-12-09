package myhomeaudio.server.handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import myhomeaudio.server.manager.NodeManager;
import myhomeaudio.server.node.Node;

/**
 * NodeHandler runs as a thread, waiting for nodes to connect through its loop.
 * 
 * @author cameron
 * 
 */
public class NodeHandler extends Thread {

	private ServerSocket nodeListenSocket; // Socket to use for listening
	private int nodeListenPort; // Port to listen on

	/**
	 * Node Handler Constructor
	 * 
	 * @param port
	 *            Give the NodeHandler the port we will be using.
	 */
	public NodeHandler(int port) {
		this.setName("NodeHandler");
		this.nodeListenSocket = null;
		this.nodeListenPort = port;

		try {
			// Start a socket on the specified port
			System.out.println("Creating Node Server Listen Socket");
			nodeListenSocket = new ServerSocket(this.nodeListenPort);
		} catch (IOException e) {
			System.out.println("NodeHandler: Unable to bind to port: " + this.nodeListenPort);
			System.out.println("Exiting");
			return;
		}
	}

	// TODO Allow node to send commands to server, server needs to listen
	public void run() {
		while (true) {
			try {

				System.out.println("Listening for nodes");
				if (this.nodeListenSocket == null) {
					System.out.println("Node Server Listen Socket Unavailable");
					return; // Stop this thread if the socket isn't available
				} else {

					// Start listening
					Socket nodeSocket = this.nodeListenSocket.accept();
					System.out.println("Node connection Found: " + nodeSocket.getPort() + "/"
							+ nodeSocket.getInetAddress().getHostAddress());

					NodeManager nm = NodeManager.getInstance();
					Node newNode = new Node(nodeSocket.getInetAddress().getHostAddress());
					if (!nm.addNode(newNode)) {
						// TODO do something, node already in list
					}

					nodeSocket.close();

					// Give the request its own thread
					/*
					 * NodeRequest2 request = new NodeRequest2(nodeSocket);
					 * 
					 * System.out.println("Starting New NodeRequest");
					 * request.start();
					 */
				}
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println("NodeHandler exited!");
				return;
			}
		}
	}

	public ServerSocket getNodeListenSocket() {
		return nodeListenSocket;
	}
}
