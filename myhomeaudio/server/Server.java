package myhomeaudio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import myhomeaudio.request.NodeRequest;

public class Server {
	protected static int PORT = 9090;

	static int numClients = 0;

	/**
	 * @param args
	 *            No parameters
	 */
	public static void main(String[] args) {
		// TODO Client Node must send server INIT message, server will then
		// create thread to handle requests
		// InitializeSetup move to new class?

		ServerSocket listenSocket = null;
		try {
			listenSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Unable to bind to port: " + PORT);
			e.printStackTrace();
		}

		try {
			while (true) {
				System.out.println("Listening");

				Socket clientSocket = listenSocket.accept();
				System.out.println("Connection Found");
				numClients++;

				NodeRequest request = new NodeRequest(clientSocket, numClients);

				// Thread thread = new Thread(request);
				System.out.println("Starting New Thread For Request");
				request.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}