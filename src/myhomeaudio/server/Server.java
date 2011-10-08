package myhomeaudio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import myhomeaudio.server.handler.ClientHandler;
import myhomeaudio.server.handler.NodeHandler;
import myhomeaudio.server.node.NodeRequest;

public class Server {
	protected static int NODE_PORT = 9090; // port that nodes will open a socket on.
	protected static int CLIENT_PORT = 8080; // port that clients (phones) will open a socket on.

	/**
	 * @param args
	 *            No parameters
	 */
	public static void main(String[] args) {

		NodeHandler nodeHandler = new NodeHandler(NODE_PORT);
		System.out.println("Starting Node Handler");
		nodeHandler.start();
		
		ClientHandler clientHandler = new ClientHandler(CLIENT_PORT);
		System.out.println("Starting Client Handler");
		clientHandler.start();

	}
}