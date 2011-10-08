package myhomeaudio.server.handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import myhomeaudio.server.http.Worker;

/**
 * ClientHandler runs as a thread and waits for Clients to connect through its loop.
 * @author cameron
 *
 */
public class ClientHandler extends Thread {
	private ServerSocket clientListenSocket;
	private int clientListenPort;
	private ArrayList<Worker> workers; // holds all the workers we can use to service client requests
	private int numWorkers;
	
	public ClientHandler(int port) {
		this.clientListenSocket = null;
		this.clientListenPort = port;
		this.workers = new ArrayList<Worker>();
		this.numWorkers = 5;
		
		try {
			clientListenSocket = new ServerSocket(this.clientListenPort);
		} catch (IOException e) {
			System.out.println("ClientHandler: Unable to bind to port: " + this.clientListenPort);
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		try {
			while (true) {
				System.out.println("Listening for clients");

				if (this.clientListenSocket == null) {
					return; // Stop this thread if the socket isn't available
				}
				
				for (int i=0; i<this.numWorkers; ++i) {
					Worker w = new Worker();
					w.start();
					workers.add(w);
				}

				Socket clientSocket = this.clientListenSocket.accept();
				System.out.println("Client connection Found");
				

			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("NodeHandler exited!");
			return;
		}
	}
}
