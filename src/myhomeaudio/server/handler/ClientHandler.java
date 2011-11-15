package myhomeaudio.server.handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.http.ClientWorker;
import myhomeaudio.server.manager.ClientManager;

/**
 * ClientHandler runs as a thread and waits for Clients to connect through its
 * loop.
 * 
 * @author cameron
 * 
 */
public class ClientHandler extends Thread {
	private ServerSocket clientListenSocket;
	private int clientListenPort;
	private ArrayList<ClientWorker> workerPool; // holds all the workers we can use to
										// service client requests
	public final int timeout = 0;
	private int maxNumWorkers;

	public ClientHandler(int port) {
		this.clientListenSocket = null;
		this.clientListenPort = port;
		this.workerPool = new ArrayList<ClientWorker>();
		this.maxNumWorkers = 5;

		try {
			//Creates ServerSocket to listen on
			clientListenSocket = new ServerSocket(this.clientListenPort);
		} catch (IOException e) {
			System.out.println("ClientHandler: Unable to bind to port: "
					+ this.clientListenPort);
			System.out.println("Exiting");
			return;
		}
		
		// Start our initial pool of Workers
		for (int i = 0; i < this.maxNumWorkers; ++i) {
			ClientWorker w = new ClientWorker(this);
			w.start();
			workerPool.add(w);
		}

	}

	public void run() {
		try {
			while (true) {
				System.out.println("Listening for clients");

				if (this.clientListenSocket == null) {
					System.out.println("Client Server Listen Socket Unavailable");
					return; // Stop this thread if the socket isn't available
				}

				Socket clientSocket = this.clientListenSocket.accept();
				System.out.println("Client connection Found");
				
				//Adds client to array
				ClientManager cm = ClientManager.getInstance();
				Client client = new Client(clientSocket.getInetAddress().getHostAddress());
				cm.addClient(client);
				
				
				
				ClientWorker worker = null;
				synchronized (workerPool) {
					if (workerPool.isEmpty()) {
						// We don't have anymore workers, add a new one to use
						worker = new ClientWorker(this);
						worker.setClientSocket(clientSocket);
						worker.start();
					} else {
						// We have a worker -- give it the socket
						worker = (ClientWorker) workerPool.get(0);
						workerPool.remove(0);
						worker.setClientSocket(clientSocket);
					}
				}

			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("ClientHandler exited !");
			return;
		}
	}

	/**
	 * Attempts to add a ClientWorker to the ClientWorker pool. It won't add another ClientWorker
	 * if there are enough in the pool already.
	 * 
	 * @param worker ClientWorker object to be added.
	 * @return Whether the ClientWorker was added to the pool.
	 */
	synchronized public boolean addWorker(ClientWorker worker) {
		if (this.getWorkerCount() >= getMaxWorkers())
		{
			// Already have enough workers!
			return false;
		}
		else {
			// We have space, let's add this ClientWorker.
			this.workerPool.add(worker);
			return true;
		}
	}

	/* Returns the number of workers in the pool
	 * 
	 * @return	size
	 * 		Size of the worker pool
	 * 
	 */
	public int getWorkerCount() {
		return this.workerPool.size();
	}

	/* Returns the maximum number of workers allowed
	 * 
	 * @return maxNumWorkers
	 * 		Maximum number of workers
	 * 
	 */
	public int getMaxWorkers() {
		return this.maxNumWorkers;
	}
}
