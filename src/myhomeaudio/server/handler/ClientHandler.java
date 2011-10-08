package myhomeaudio.server.handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import myhomeaudio.server.http.Worker;

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
	private ArrayList<Worker> workerPool; // holds all the workers we can use to
										// service client requests
	public final int timeout = 0;
	private int maxNumWorkers;

	public ClientHandler(int port) {
		this.clientListenSocket = null;
		this.clientListenPort = port;
		this.workerPool = new ArrayList<Worker>();
		this.maxNumWorkers = 5;

		try {
			clientListenSocket = new ServerSocket(this.clientListenPort);
		} catch (IOException e) {
			System.out.println("ClientHandler: Unable to bind to port: "
					+ this.clientListenPort);
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

				for (int i = 0; i < this.maxNumWorkers; ++i) {
					Worker w = new Worker(this);
					w.start();
					workerPool.add(w);
				}

				Socket clientSocket = this.clientListenSocket.accept();
				System.out.println("Client connection Found");
				Worker worker = null;
				synchronized (workerPool) {
					if (workerPool.isEmpty()) {
						// We don't have anymore workers, add a new one to use
						worker = new Worker(this);
						worker.setClientSocket(clientSocket);
						worker.start();
					} else {
						// We have a worker -- give it the socket
						worker = (Worker) workerPool.get(0);
						workerPool.remove(0);
						worker.setClientSocket(clientSocket);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("NodeHandler exited!");
			return;
		}
	}

	/**
	 * Attempts to add a Worker to the Worker pool. It won't add another Worker
	 * if there are enough in the pool already.
	 * 
	 * @param worker Worker object to be added.
	 * @return Whether the Worker was added to the pool.
	 */
	synchronized public boolean addWorker(Worker worker) {
		if (this.getWorkerCount() == getMaxWorkers())
		{
			// Already have enough workers!
			return false;
		}
		else {
			// We have space, let's add this Worker.
			this.workerPool.add(worker);
			return true;
		}
	}

	public int getWorkerCount() {
		return this.workerPool.size();
	}

	public int getMaxWorkers() {
		return this.maxNumWorkers;
	}
}
