package myhomeaudio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import myhomeaudio.server.handler.NodeHandler;
import myhomeaudio.server.request.NodeRequest;

public class Server {
	protected static int NODE_PORT = 9090; // port that nodes will open a socket on.
	protected static int CLIENT_PORT = 8080; // port that clients (phones) will open a socket on.

	/**
	 * @param args
	 *            No parameters
	 */
	public static void main(String[] args) {
		// TODO Client Node must send server INIT message, server will then
		// create thread to handle requests
		// InitializeSetup move to new class?

		
		ServerSocket clientListenSocket = null;
		
		try {
			clientListenSocket = new ServerSocket(CLIENT_PORT);
		} catch (IOException e) {
			System.out.println("Unable to bind to port: " + CLIENT_PORT);
			
		}

		NodeHandler nodeHandler = new NodeHandler(NODE_PORT);
		System.out.println("Starting Node Handler");
		nodeHandler.start();

	}
}