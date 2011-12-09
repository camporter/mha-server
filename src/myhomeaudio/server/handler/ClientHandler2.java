package myhomeaudio.server.handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.http.ClientWorker;
import myhomeaudio.server.manager.ClientManager;

/**
 * ClientHandler2 runs as a thread and waits for Clients to connect through its
 * loop.
 * 
 * @author Cameron
 * 
 */
public class ClientHandler2 extends Thread {

	private ServerSocket clientListenSocket;
	private int clientListenPort;

	/*
	 * holds all the workers we can use to service client requests
	 */
	private ArrayList<ClientWorker> workerPool;

	private int maxNumWorkers;

	public ClientHandler2(int port) {
		this.setName("ClientHandler2");
		
		this.clientListenSocket = null;
		this.clientListenPort = port;
		this.workerPool = new ArrayList<ClientWorker>();
		this.maxNumWorkers = 5;

		try {
			// Creates ServerSocket to listen on
			clientListenSocket = new ServerSocket(this.clientListenPort);
		} catch (IOException e) {
			System.out.println("ClientHandler2: Unable to bind to port: " + this.clientListenPort);
			System.out.println("Exiting");
			return;
		}

		// Start our an initial pool of ClientWorkers
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
					// Stop this thread if the socket isn't available
					return;
				}

				// Wait until a connection is found
				Socket clientSocket = this.clientListenSocket.accept();
				System.out.println("Client connection Found: " + clientSocket.getPort() + "/"
						+ clientSocket.getInetAddress().getHostAddress());

				// Add the client to the ClientManager
				ClientManager cm = ClientManager.getInstance();
				Client client = new Client(clientSocket.getInetAddress().getHostAddress());
				cm.addClient(client);

				// Make a ClientWorker handle the client's request
				ClientWorker worker = null;
				synchronized (workerPool) {
					if (workerPool.isEmpty()) {
						/*
						 * We don't have any unused workers, add a new one to
						 * use
						 */
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
			// e.printStackTrace();
			System.out.println("ClientHandler2 exited !");
			return;
		}
	}

	/**
	 * Attempts to add a ClientWorker to the ClientWorker pool. It won't add
	 * another ClientWorker if there are enough in the pool already.
	 * 
	 * @param worker
	 *            ClientWorker object to be added.
	 * @return Whether the ClientWorker was added to the pool.
	 */
	synchronized public boolean addWorker(ClientWorker worker) {
		if (this.getWorkerCount() >= getMaxWorkers()) {
			// Already have enough workers!
			return false;
		} else {
			// We have space, let's add this ClientWorker.
			this.workerPool.add(worker);
			return true;
		}
	}

	/**
	 * Returns the number of workers in the pool
	 * 
	 * @return size Size of the worker pool
	 * 
	 */
	public int getWorkerCount() {
		return this.workerPool.size();
	}

	/**
	 * Returns the maximum number of workers allowed
	 * 
	 * @return maxNumWorkers Maximum number of workers
	 * 
	 */
	public int getMaxWorkers() {
		return this.maxNumWorkers;
	}
}
